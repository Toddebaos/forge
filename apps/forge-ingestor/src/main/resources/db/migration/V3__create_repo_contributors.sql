-- En contributor kan bidra till många repos och ett repo har många contributors
CREATE TABLE IF NOT EXISTS repo_contributors (
    repo_id         BIGINT NOT NULL REFERENCES repositories(github_id),
    contributor_id  BIGINT NOT NULL REFERENCES contributors(github_id),
    commit_count    INTEGER DEFAULT 0,
    PRIMARY KEY (repo_id, contributor_id)
);