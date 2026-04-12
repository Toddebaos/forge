package com.forge.ingestor.client;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class GitHubClient {

    private final GitHub github;
    private final String username;

    public GitHubClient(
            @Value("${github.token}") String token,
            @Value("${github.username}") String username) throws IOException {
        this.github = new GitHubBuilder().withOAuthToken(token).build();
        this.username = username;
        log.info("GitHubClient initialized for user: {}", username);
    }

    // Hämtar alla publika och privata repos för autentiserad användare
    public List<GHRepository> fetchAllRepos() throws IOException {
        log.info("Fetching all repos for {}", username);
        return github.getUser(username)
                .listRepositories()
                .withPageSize(5)
                .toList();
    }

    // Hämtar commits för ett specifikt repo — begränsat till senaste 100
    public List<GHCommit> fetchCommits(GHRepository repo) throws IOException {
        log.info("Fetching commits for {}", repo.getFullName());
        return repo.listCommits()
                .withPageSize(5)
                .toList();
    }

    // Hämtar contributors för ett repo
    public List<GHRepository.Contributor> fetchContributors(GHRepository repo) throws IOException {
        log.info("Fetching contributors for {}", repo.getFullName());
        return repo.listContributors().toList();
    }
}