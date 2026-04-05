package com.forge.api.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "contributors")
public class Contributor {

    @Id
    @Column(name = "github_id")
    private Long githubId;

    private String login;
    private String avatarUrl;
    private String htmlUrl;
    private String name;
    private String company;
    private String location;
}