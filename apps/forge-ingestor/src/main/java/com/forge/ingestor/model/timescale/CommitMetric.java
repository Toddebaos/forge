package com.forge.ingestor.model.timescale;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "commit_metrics")
public class CommitMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TimescaleDB partitionerar tabellen på denna kolumn automatiskt
    @Column(nullable = false)
    private Instant timestamp;

    @Column(name = "repo_full_name", nullable = false)
    private String repoFullName;

    @Column(name = "commit_count")
    private int commitCount;        // antal commits i detta tidsintervall

    @Column(name = "additions")
    private int additions;          // antal tillagda rader kod

    @Column(name = "deletions")
    private int deletions;          // antal borttagna rader kod

    private String author;          // GitHub-login för commit-författaren

    @Column(name = "commit_sha" , length = 40)
    private String commitSha;
}