package com.forge.ingestor.model.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "repositories")
public class GitHubRepo {

    @Id
    @Column(name = "github_id")
    private Long githubId;          // GitHub's eget ID — vi använder det som primärnyckel

    @Column(nullable = false)
    private String name;

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;        // t.ex. "todde/forge"

    private String description;
    private String language;
    private String htmlUrl;

    private int stars;
    private int forks;
    private int openIssues;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "last_synced_at")
    private Instant lastSyncedAt;   // när Forge senast hämtade data om detta repo
}