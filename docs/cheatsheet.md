# Forge — Cheatsheet & Arbetsflöden

## Infrastruktur
| | |
|---|---|
| **Server IP** | `37.27.205.132` |
| **SSH** | `ssh root@37.27.205.132` |
| **Domän** | `toddemanos.com` |
| **KUBECONFIG (server)** | `/etc/rancher/k3s/k3s.yaml` |
| **KUBECONFIG (Mac)** | `~/server/hetzner-kubeconfig.yaml` |
| **KUBECONFIG (PC)** | `C:\Users\todde\server\hetzner-kubeconfig.yaml` |

### Tjänster
| Tjänst | URL |
|---|---|
| Frontend | https://toddemanos.com |
| API | https://api.toddemanos.com |
| Grafana | https://grafana.toddemanos.com |
| ArgoCD | https://argocd.toddemanos.com |

---

## Sätt KUBECONFIG
```bash
# Server
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# Mac
export KUBECONFIG=~/server/hetzner-kubeconfig.yaml

# PC (PowerShell)
$env:KUBECONFIG="C:\Users\todde\server\hetzner-kubeconfig.yaml"
```

---

## Lägga till en ny komponent (ArgoCD)
1. Skapa `infra/argocd/apps/namn.yaml`
2. Pusha till GitHub:
```bash
git add .
git commit -m "feat: add namn"
git push
```
3. Synka ArgoCD:
```bash
argocd app sync namn
```
4. Verifiera:
```bash
kubectl get pods -n monitoring   # eller todde/argocd etc.
```

---

## Synka ArgoCD
```bash
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# Starta port-forward (kör i bakgrunden)
kubectl port-forward svc/argocd-server -n argocd 8080:443 &
sleep 3

# Logga in
argocd login localhost:8080 \
  --username admin \
  --password $(kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d) \
  --insecure

# Synka app
argocd app sync <appnamn>

# Lista alla appar
argocd app list
```

---

## Uppdatera Prometheus/Grafana (Helm)
```bash
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
helm upgrade prometheus prometheus-community/kube-prometheus-stack \
  -n monitoring \
  -f - <<EOF
# dina values här
EOF
```

### Hämta nuvarande values
```bash
helm get values prometheus -n monitoring
```

---

## Felsöka en pod
```bash
# Se status
kubectl get pods -n <namespace>

# Se events och detaljer
kubectl describe pod <pod-namn> -n <namespace>

# Se loggar
kubectl logs <pod-namn> -n <namespace> --tail=30

# Följa loggar live
kubectl logs <pod-namn> -n <namespace> -f

# Starta om deployment
kubectl rollout restart deployment/<namn> -n <namespace>

# Starta om statefulset
kubectl rollout restart statefulset/<namn> -n <namespace>

# Starta om daemonset
kubectl rollout restart daemonset/<namn> -n <namespace>
```

---

## Felsöka en exporter
```bash
# Port-forward till exporter
kubectl port-forward -n monitoring deployment/<exporter-namn> 9090:9090 &
sleep 2

# Kolla metrics
curl -s http://localhost:9090/metrics | grep "_up"

# Vanliga exporter-portar
# postgres:        9187
# redis:           9121
# mongodb:         9216
# elasticsearch:   9114
```

### Verifiera i Grafana
Grafana → Explore → Prometheus → sök på `<exporter>_up`
- Värde `1` = fungerar
- Värde `0` = anslutningsproblem
- Saknas helt = Prometheus scraper inte

---

## Kolla resurser
```bash
kubectl top nodes
kubectl top pods -n monitoring --sort-by=memory
kubectl top pods -n todde --sort-by=memory
kubectl top pods -A --sort-by=memory | head -15
```

---

## Secrets
```bash
# Läsa ett secret
kubectl get secret <namn> -n <namespace> -o jsonpath='{.data.<nyckel>}' | base64 -d

# Uppdatera ett secret
kubectl create secret generic <namn> \
  -n <namespace> \
  --from-literal=<nyckel>=<värde> \
  --dry-run=client -o yaml | kubectl apply -f -
```

### Secrets i klustret
| Secret | Namespace | Nyckel | Värde |
|---|---|---|---|
| forge-postgres-credentials | todde | password | forge-postgres-secret-2026 |
| forge-redis-credentials | todde | password | forge-redis-secret-2026 |
| forge-elasticsearch-credentials | todde | password | forge-elastic-secret-2026 |
| forge-mongodb-credentials | todde | mongodb-root-password | forge-mongo-secret-2026 |
| grafana-admin-secret | monitoring | admin-user / admin-password | admin / (ditt lösenord) |

---

## MongoDB
```bash
# Anslut direkt
kubectl exec -n todde forge-mongodb-0 -- mongosh

# Lista användare
kubectl exec -n todde forge-mongodb-0 -- mongosh \
  --eval "db.getSiblingDB('admin').getUsers()"

# Ändra lösenord
kubectl exec -n todde forge-mongodb-0 -- mongosh \
  --eval "db.getSiblingDB('admin').changeUserPassword('root', 'nyttlosenord')"
```

---

## PostgreSQL
```bash
# Anslut direkt
kubectl exec -n todde forge-postgres-0 -- psql -U postgres

# Lista databaser
kubectl exec -n todde forge-postgres-0 -- psql -U postgres -c "\l"
```

---

## Git-arbetsflöde
```bash
git pull
git add .
git commit -m "feat/fix: beskrivning"
git push
```

CI triggas automatiskt vid push till `apps/forge-api/**` eller `apps/forge-ingestor/**`.

---

## Återstående uppgifter — Phase 2
- [ ] Fixa MongoDB-exporter (auth-problem)
- [ ] Grafana dashboards för databaser
- [ ] PostgreSQL backups till S3
- [ ] Cloudflare Zero Trust Access för Grafana + ArgoCD
- [ ] Network Policies
- [ ] Pod Security (securityContext)
- [ ] Falco runtime security
- [ ] Trivy image scanning i CI