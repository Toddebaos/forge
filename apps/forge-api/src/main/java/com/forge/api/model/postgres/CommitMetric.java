package com.forge.api.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.Instant;

@Data
@Entity
@Table(name ="commit_metrics")
public class CommitMetric {

    @Id
    @Column(name = "id")
    private BigInteger id;

    private Long additions;
    private String author;
    private Long commitCount;
    private Long deletions;
    private String repoFullName;

    @Column(name = "timestamp")
    private Instant timestamp;

    private String commitSha;
    
}
