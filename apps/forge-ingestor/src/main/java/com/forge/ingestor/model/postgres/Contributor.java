package com.forge.ingestor.model.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contributors")
public class Contributor {

    @Id
    @Column(name = "github_id")
    private Long githubId;

    @Column(nullable = false, unique = true)
    private String login;           // GitHub-användarnamn

    private String avatarUrl;
    private String htmlUrl;
    private String name;
    private String company;
    private String location;
}
