package com.forge.api.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.Instant;

@Data
public class CommitMetricDTO {
    private BigInteger id;
    private Long additions;
    private String author;
    private Long commitCount;
    private Long deletions;
    private String repoFullName;
    private Instant timeStamp;

}
