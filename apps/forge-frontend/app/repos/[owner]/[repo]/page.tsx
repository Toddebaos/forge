import Link from "next/link";
import { fetchRepo } from "../../../lib/api";

function Detail({ label, value }: { label: string; value: string | number | null }) {
  if (value === null || value === undefined) return null;
  return (
    <div className="flex justify-between border-b border-border py-3 last:border-0">
      <span className="text-muted">{label}</span>
      <span className="font-medium">{typeof value === "number" ? value.toLocaleString() : value}</span>
    </div>
  );
}

export default async function RepoDetailPage({
  params,
}: {
  params: Promise<{ owner: string; repo: string }>;
}) {
  const { owner, repo: repoName } = await params;

  let repo;
  let error: string | null = null;

  try {
    repo = await fetchRepo(owner, repoName);
  } catch (e) {
    error = e instanceof Error ? e.message : "Failed to fetch repo";
  }

  return (
    <div className="mx-auto max-w-3xl px-6 py-10">
      <Link
        href="/"
        className="mb-6 inline-flex items-center gap-1 text-sm text-muted hover:text-foreground transition-colors"
      >
        ← Back to dashboard
      </Link>

      {error || !repo ? (
        <div className="rounded-lg border border-red-500/30 bg-red-500/5 p-6 text-center">
          <p className="font-medium text-red-400">Failed to load repository</p>
          <p className="mt-1 text-sm text-muted">{error}</p>
        </div>
      ) : (
        <>
          <div className="mb-8">
            <h1 className="text-3xl font-bold tracking-tight">{repo.fullName}</h1>
            {repo.description && (
              <p className="mt-2 text-muted">{repo.description}</p>
            )}
          </div>

          <div className="mb-8 grid grid-cols-3 gap-4">
            <div className="rounded-lg border border-border bg-card p-5 text-center">
              <p className="text-2xl font-semibold">{repo.stars.toLocaleString()}</p>
              <p className="text-sm text-muted">Stars</p>
            </div>
            <div className="rounded-lg border border-border bg-card p-5 text-center">
              <p className="text-2xl font-semibold">{repo.forks.toLocaleString()}</p>
              <p className="text-sm text-muted">Forks</p>
            </div>
            <div className="rounded-lg border border-border bg-card p-5 text-center">
              <p className="text-2xl font-semibold">{repo.openIssues}</p>
              <p className="text-sm text-muted">Open Issues</p>
            </div>
          </div>

          <div className="rounded-lg border border-border bg-card p-5">
            <h2 className="mb-3 font-semibold">Details</h2>
            <Detail label="Language" value={repo.language} />
            <Detail label="Full Name" value={repo.fullName} />
            <Detail
              label="Last Updated"
              value={new Date(repo.updatedAt).toLocaleDateString("en-US", {
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            />
            <Detail
              label="Last Synced"
              value={new Date(repo.lastSyncedAt).toLocaleDateString("en-US", {
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            />
          </div>

          <div className="mt-6">
            <a
              href={repo.htmlUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-2 rounded-lg bg-accent px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-accent-hover"
            >
              View on GitHub →
            </a>
          </div>
        </>
      )}
    </div>
  );
}
