package com.forge.api.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class RepoDTO {
    private Long id;
    private String name;
    private String fullName;
    private String description;
    private String language;
    private String htmlUrl;
    private int stars;
    private int forks;
    private int openIssues;
    private Instant updatedAt;
    private Instant lastSyncedAt;
}