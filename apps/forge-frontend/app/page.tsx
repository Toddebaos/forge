import Link from "next/link";
import { fetchRepos, type RepoDTO } from "./lib/api";

function StatCard({ label, value }: { label: string; value: string | number }) {
  return (
    <div className="rounded-lg border border-border bg-card p-5">
      <p className="text-sm text-muted">{label}</p>
      <p className="mt-1 text-2xl font-semibold">{value}</p>
    </div>
  );
}

function LanguageBadge({ language }: { language: string | null }) {
  if (!language) return null;
  return (
    <span className="inline-block rounded-full bg-accent/10 px-2.5 py-0.5 text-xs font-medium text-accent">
      {language}
    </span>
  );
}

function RepoCard({ repo }: { repo: RepoDTO }) {
  const [owner, name] = repo.fullName.split("/");
  return (
    <Link
      href={`/repos/${owner}/${name}`}
      className="group rounded-lg border border-border bg-card p-5 transition-colors hover:bg-card-hover"
    >
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0 flex-1">
          <h3 className="truncate font-semibold group-hover:text-accent transition-colors">
            {repo.fullName}
          </h3>
          {repo.description && (
            <p className="mt-1 line-clamp-2 text-sm text-muted">
              {repo.description}
            </p>
          )}
        </div>
        <LanguageBadge language={repo.language} />
      </div>
      <div className="mt-4 flex items-center gap-5 text-sm text-muted">
        <span title="Stars">★ {repo.stars.toLocaleString()}</span>
        <span title="Forks">⑂ {repo.forks.toLocaleString()}</span>
        <span title="Open issues">◉ {repo.openIssues}</span>
      </div>
    </Link>
  );
}

function LanguageFilter({
  languages,
  active,
}: {
  languages: string[];
  active: string | undefined;
}) {
  return (
    <div className="flex flex-wrap gap-2">
      <Link
        href="/"
        className={`rounded-full border px-3 py-1 text-sm transition-colors ${
          !active
            ? "border-accent bg-accent/10 text-accent"
            : "border-border text-muted hover:text-foreground"
        }`}
      >
        All
      </Link>
      {languages.map((lang) => (
        <Link
          key={lang}
          href={`/?language=${encodeURIComponent(lang)}`}
          className={`rounded-full border px-3 py-1 text-sm transition-colors ${
            active === lang
              ? "border-accent bg-accent/10 text-accent"
              : "border-border text-muted hover:text-foreground"
          }`}
        >
          {lang}
        </Link>
      ))}
    </div>
  );
}

export default async function DashboardPage({
  searchParams,
}: {
  searchParams: Promise<{ language?: string }>;
}) {
  const { language } = await searchParams;

  let repos: RepoDTO[] = [];
  let error: string | null = null;

  try {
    repos = await fetchRepos();
  } catch (e) {
    error = e instanceof Error ? e.message : "Failed to fetch data";
  }

  const languages = [
    ...new Set(repos.map((r) => r.language).filter(Boolean) as string[]),
  ].sort();

  const filtered = language
    ? repos.filter((r) => r.language === language)
    : repos;

  const totalStars = repos.reduce((sum, r) => sum + r.stars, 0);
  const totalForks = repos.reduce((sum, r) => sum + r.forks, 0);

  return (
    <div className="mx-auto max-w-7xl px-6 py-10">
      <div className="mb-8">
        <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
        <p className="mt-1 text-muted">
          GitHub repository analytics — powered by Forge
        </p>
      </div>

      {error ? (
        <div className="rounded-lg border border-red-500/30 bg-red-500/5 p-6 text-center">
          <p className="font-medium text-red-400">Unable to connect to Forge API</p>
          <p className="mt-1 text-sm text-muted">{error}</p>
          <p className="mt-3 text-xs text-muted">
            Make sure forge-api is running and NEXT_PUBLIC_API_URL is set
          </p>
        </div>
      ) : (
        <>
          <div className="mb-8 grid grid-cols-2 gap-4 sm:grid-cols-4">
            <StatCard label="Repositories" value={repos.length} />
            <StatCard label="Total Stars" value={totalStars.toLocaleString()} />
            <StatCard label="Total Forks" value={totalForks.toLocaleString()} />
            <StatCard label="Languages" value={languages.length} />
          </div>

          <div className="mb-6">
            <LanguageFilter languages={languages} active={language} />
          </div>

          {filtered.length === 0 ? (
            <p className="py-12 text-center text-muted">No repositories found.</p>
          ) : (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {filtered.map((repo) => (
                <RepoCard key={repo.id} repo={repo} />
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
}
