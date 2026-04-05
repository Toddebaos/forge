package com.forge.api.repository.postgres;

import com.forge.api.model.postgres.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    Optional<Contributor> findByLogin(String login);
}