package com.forge.api.dto;

import lombok.Data;

@Data
public class ContributorDTO {
    private Long id;
    private String login;
    private String avatarUrl;
    private String htmlUrl;
    private String name;
    private String location;
}