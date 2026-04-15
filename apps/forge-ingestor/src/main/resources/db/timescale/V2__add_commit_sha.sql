ALTER TABLE commit_metrics ADD COLUMN IF NOT EXISTS commit_sha VARCHAR(40);
CREATE UNIQUE INDEX IF NOT EXISTS idx_commit_metrics_sha 
    ON commit_metrics(repo_full_name, commit_sha);