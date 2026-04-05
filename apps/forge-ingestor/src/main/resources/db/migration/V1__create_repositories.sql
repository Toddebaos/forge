CREATE TABLE IF NOT EXISTS repositories (
    github_id       BIGINT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255) NOT NULL UNIQUE,
    description     TEXT,
    language        VARCHAR(100),
    html_url        VARCHAR(500),
    stars           INTEGER DEFAULT 0,
    forks           INTEGER DEFAULT 0,
    open_issues     INTEGER DEFAULT 0,
    is_private      BOOLEAN DEFAULT false,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    last_synced_at  TIMESTAMPTZ
);

CREATE INDEX idx_repositories_language ON repositories(language);
CREATE INDEX idx_repositories_updated_at ON repositories(updated_at DESC);