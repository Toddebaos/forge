package com.forge.ingestor.service;

import com.forge.ingestor.model.timescale.CommitMetric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "timescale.enabled", havingValue = "true")
public class TimescaleService {

    // JdbcTemplate pekar på TimescaleDB-datasourcen via config
    private final JdbcTemplate timescaleJdbcTemplate;

    public void save(CommitMetric metric) {
        // Använder INSERT ... ON CONFLICT DO NOTHING
        // samma commit ska inte dupliceras om ingestor kör igen
        timescaleJdbcTemplate.update("""
            INSERT INTO commit_metrics
                (timestamp, repo_full_name, commit_count, additions, deletions, author, commit_sha)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (repo_full_name, commit_sha) DO NOTHING
            """,
            Timestamp.from(metric.getTimestamp()),
            metric.getRepoFullName(),
            metric.getCommitCount(),
            metric.getAdditions(),
            metric.getDeletions(),
            metric.getAuthor(),
            metric.getCommitSha()
        );
    }
}