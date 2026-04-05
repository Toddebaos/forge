package com.forge.ingestor.repository.postgres;

import com.forge.ingestor.model.postgres.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    boolean existsByLogin(String login);
}