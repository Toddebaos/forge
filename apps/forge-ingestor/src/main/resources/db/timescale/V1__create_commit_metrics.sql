-- Skapa tabellen först
CREATE TABLE IF NOT EXISTS commit_metrics (
    id              BIGSERIAL,
    timestamp       TIMESTAMPTZ NOT NULL,
    repo_full_name  VARCHAR(255) NOT NULL,
    commit_count    INTEGER DEFAULT 1,
    additions       INTEGER DEFAULT 0,
    deletions       INTEGER DEFAULT 0,
    author          VARCHAR(255),
    PRIMARY KEY (id, timestamp)
);

-- Konvertera till hypertabell — detta är TimescaleDB-magin
-- Partitionerar automatiskt data per tidsintervall (chunk_time_interval = 7 dagar)
SELECT create_hypertable('commit_metrics', 'timestamp', if_not_exists => TRUE);

-- Komprimera data äldre än 30 dagar automatiskt
ALTER TABLE commit_metrics SET (
    timescaledb.compress,
    timescaledb.compress_orderby = 'timestamp DESC',
    timescaledb.compress_segmentby = 'repo_full_name'
);

SELECT add_compression_policy('commit_metrics', INTERVAL '30 days');

CREATE INDEX idx_commit_metrics_repo ON commit_metrics(repo_full_name, timestamp DESC);
CREATE INDEX idx_commit_metrics_author ON commit_metrics(author, timestamp DESC);