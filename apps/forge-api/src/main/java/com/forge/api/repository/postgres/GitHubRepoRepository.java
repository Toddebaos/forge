package com.forge.api.repository.postgres;

import com.forge.api.model.postgres.GitHubRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GitHubRepoRepository extends JpaRepository<GitHubRepo, Long> {
    Optional<GitHubRepo> findByFullName(String fullName);
    List<GitHubRepo> findByLanguageOrderByStarsDesc(String language);
    List<GitHubRepo> findAllByOrderByStarsDesc();
}