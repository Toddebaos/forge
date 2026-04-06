const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export interface RepoDTO {
  id: number;
  name: string;
  fullName: string;
  description: string | null;
  language: string | null;
  htmlUrl: string;
  stars: number;
  forks: number;
  openIssues: number;
  updatedAt: string;
  lastSyncedAt: string;
}

export async function fetchRepos(): Promise<RepoDTO[]> {
  const res = await fetch(`${API_BASE}/api/v1/repos`, {
    next: { revalidate: 60 },
  });
  if (!res.ok) throw new Error(`Failed to fetch repos: ${res.status}`);
  return res.json();
}

export async function fetchRepo(
  owner: string,
  repo: string,
): Promise<RepoDTO> {
  const res = await fetch(`${API_BASE}/api/v1/repos/${owner}/${repo}`, {
    next: { revalidate: 60 },
  });
  if (!res.ok) throw new Error(`Failed to fetch repo: ${res.status}`);
  return res.json();
}

export async function fetchReposByLanguage(
  language: string,
): Promise<RepoDTO[]> {
  const res = await fetch(
    `${API_BASE}/api/v1/repos/by-language?language=${encodeURIComponent(language)}`,
    { next: { revalidate: 60 } },
  );
  if (!res.ok) throw new Error(`Failed to fetch repos: ${res.status}`);
  return res.json();
}
