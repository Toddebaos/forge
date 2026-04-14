package com.forge.api.repository.postgres;

import com.forge.api.model.postgres.CommitMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;


@Repository
public interface CommitMetricRepository extends JpaRepository<CommitMetric, BigInteger> {
    //hämta repo och sortera på timestamp
    List<CommitMetric> findByRepoFullNameOrderByTimestampDesc(String repoAndOwnerName);
}
