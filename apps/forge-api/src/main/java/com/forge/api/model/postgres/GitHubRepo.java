package com.forge.api.model.postgres;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "repositories")
public class GitHubRepo {

    @Id
    @Column(name = "github_id")
    private Long githubId;

    private String name;

    @Column(name = "full_name")
    private String fullName;

    private String description;
    private String language;
    private String htmlUrl;
    private int stars;
    private int forks;

    @Column(name = "open_issues")
    private int openIssues;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "last_synced_at")
    private Instant lastSyncedAt;
}