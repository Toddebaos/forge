package com.forge.api.repository.postgres;

import com.forge.api.model.postgres.CommitMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.time.Instant;


@Repository
public interface CommitMetricRepository extends JpaRepository<CommitMetric, Long> {
    Optional<CommitMetric> getCommitsByRepo(String repoFullName);
    //hämta repo och sortera på timestamp
    List<CommitMetric> findByRepoFullNameOrderByTimeStampDesc(String repoFullName);
}
