package com.forge.api.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.Instant;

@Data
public class CommitMetricDTO {
    private BigInteger id;
    private Long addidions;
    private String author;
    private Long commit_count;
    private Long deletions;
    private String repoFullName;
    private Instant timeStamp;

}
