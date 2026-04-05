CREATE TABLE IF NOT EXISTS contributors (
    github_id   BIGINT PRIMARY KEY,
    login       VARCHAR(255) NOT NULL UNIQUE,
    avatar_url  VARCHAR(500),
    html_url    VARCHAR(500),
    name        VARCHAR(255),
    company     VARCHAR(255),
    location    VARCHAR(255)
);

CREATE INDEX idx_contributors_login ON contributors(login);