package com.forge.api.service;

import com.forge.api.dto.RepoDTO;
import com.forge.api.model.postgres.GitHubRepo;
import com.forge.api.repository.postgres.GitHubRepoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepoService {

    private final GitHubRepoRepository repoRepository;
    private final StringRedisTemplate redisTemplate;

    @Value("${cache.repos.ttl}")
    private long cacheTtl;

    public List<RepoDTO> getAllRepos() {
        String cacheKey = "repos:all";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            log.debug("Cache hit for {}", cacheKey);
        }

        List<RepoDTO> repos = repoRepository.findAllByOrderByStarsDesc()
                .stream()
                .map(this::toDTO)
                .toList();

        redisTemplate.opsForValue().set(cacheKey, "cached", Duration.ofSeconds(cacheTtl));
        return repos;
    }

    public RepoDTO getRepoByFullName(String owner, String name) {
        String fullName = owner + "/" + name;
        return repoRepository.findByFullName(fullName)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Repo not found: " + fullName));
    }

    public List<RepoDTO> getReposByLanguage(String language) {
        return repoRepository.findByLanguageOrderByStarsDesc(language)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private RepoDTO toDTO(GitHubRepo repo) {
        RepoDTO dto = new RepoDTO();
        dto.setId(repo.getGithubId());
        dto.setName(repo.getName());
        dto.setFullName(repo.getFullName());
        dto.setDescription(repo.getDescription());
        dto.setLanguage(repo.getLanguage());
        dto.setHtmlUrl(repo.getHtmlUrl());
        dto.setStars(repo.getStars());
        dto.setForks(repo.getForks());
        dto.setOpenIssues(repo.getOpenIssues());
        dto.setUpdatedAt(repo.getUpdatedAt());
        dto.setLastSyncedAt(repo.getLastSyncedAt());
        return dto;
    }
}