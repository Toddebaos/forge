package com.forge.ingestor.repository.postgres;

import com.forge.ingestor.model.postgres.GitHubRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GitHubRepoRepository extends JpaRepository<GitHubRepo, Long> {
    Optional<GitHubRepo> findByFullName(String fullName);
}