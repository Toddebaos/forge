package com.forge.ingestor.service;

import com.forge.ingestor.client.GitHubClient;
import com.forge.ingestor.model.elasticsearch.SearchableCommit;
import com.forge.ingestor.model.postgres.Contributor;
import com.forge.ingestor.model.postgres.GitHubRepo;
import com.forge.ingestor.model.timescale.CommitMetric;
import com.forge.ingestor.repository.elasticsearch.SearchableCommitRepository;
import com.forge.ingestor.repository.postgres.ContributorRepository;
import com.forge.ingestor.repository.postgres.GitHubRepoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngestionService {

    private final GitHubClient gitHubClient;
    private final GitHubRepoRepository repoRepository;
    private final ContributorRepository contributorRepository;
    private final SearchableCommitRepository searchableCommitRepository;
    private final TimescaleService timescaleService;
    private final StringRedisTemplate redisTemplate;

    public void ingestAll() {
        log.info("Starting full ingestion cycle");
        try {
            List<GHRepository> repos = gitHubClient.fetchAllRepos();
            for (GHRepository ghRepo : repos) {
                ingestRepo(ghRepo);
            }
            log.info("Ingestion cycle complete — {} repos processed", repos.size());
        } catch (IOException e) {
            log.error("Ingestion failed", e);
        }
    }

    private void ingestRepo(GHRepository ghRepo) throws IOException {
        log.info("Ingesting repo: {}", ghRepo.getFullName());

        // 1. PostgreSQL — spara/uppdatera repo
        GitHubRepo repo = mapToRepo(ghRepo);
        repoRepository.save(repo);

        // 2. PostgreSQL — spara contributors
        gitHubClient.fetchContributors(ghRepo).forEach(c -> {
            if (!contributorRepository.existsByLogin(c.getLogin())) {
                Contributor contributor = new Contributor();
                contributor.setGithubId((long) c.getId());
                contributor.setLogin(c.getLogin());
                contributor.setAvatarUrl(c.getAvatarUrl());
                contributor.setHtmlUrl(c.getHtmlUrl().toString());
                contributorRepository.save(contributor);
            }
        });

        // 3. Commits — fan-out till TimescaleDB + Elasticsearch
        List<GHCommit> commits = gitHubClient.fetchCommits(ghRepo);
        for (GHCommit ghCommit : commits) {
            ingestCommit(ghCommit, ghRepo.getFullName());
        }

        // 4. Redis — invaliderera cache för detta repo
        // API:et cachar repo-data — vi signalerar att cachen är stale
        redisTemplate.delete("repo:" + ghRepo.getFullName());
        redisTemplate.opsForValue().set(
            "repo:last_synced:" + ghRepo.getFullName(),
            Instant.now().toString()
        );

        log.info("Repo {} ingested successfully", ghRepo.getFullName());
    }

    private void ingestCommit(GHCommit ghCommit, String repoFullName) throws IOException {
        GHCommit.ShortInfo info = ghCommit.getCommitShortInfo();

        // TimescaleDB — commit-metrik med tidsstämpel
        CommitMetric metric = new CommitMetric();
        metric.setTimestamp(info.getCommitDate().toInstant());
        metric.setRepoFullName(repoFullName);
        metric.setCommitCount(1);
        metric.setAdditions(ghCommit.getLinesAdded());
        metric.setDeletions(ghCommit.getLinesDeleted());
        metric.setAuthor(info.getAuthor().getName());
        timescaleService.save(metric);

        // Elasticsearch — indexera commit-meddelandet för sökning
        SearchableCommit searchable = new SearchableCommit();
        searchable.setSha(ghCommit.getSHA1());
        searchable.setMessage(info.getMessage());
        searchable.setRepoFullName(repoFullName);
        searchable.setAuthor(info.getAuthor().getName());
        searchable.setCommittedAt(info.getCommitDate().toInstant());
        searchable.setHtmlUrl(ghCommit.getHtmlUrl().toString());
        searchableCommitRepository.save(searchable);
    }

    private GitHubRepo mapToRepo(GHRepository ghRepo) throws IOException {
        GitHubRepo repo = new GitHubRepo();
        repo.setGithubId(ghRepo.getId());
        repo.setName(ghRepo.getName());
        repo.setFullName(ghRepo.getFullName());
        repo.setDescription(ghRepo.getDescription());
        repo.setLanguage(ghRepo.getLanguage());
        repo.setHtmlUrl(ghRepo.getHtmlUrl().toString());
        repo.setStars(ghRepo.getStargazersCount());
        repo.setForks(ghRepo.getForksCount());
        repo.setOpenIssues(ghRepo.getOpenIssueCount());
        repo.setPrivate(ghRepo.isPrivate());
        repo.setCreatedAt(ghRepo.getCreatedAt().toInstant());
        repo.setUpdatedAt(ghRepo.getUpdatedAt().toInstant());
        repo.setLastSyncedAt(Instant.now());
        return repo;
    }
}