# Kubernetes Deployment Guide

Bu dizin, Öğrenci Kayıt Sistemi'nin Kubernetes ortamında çalıştırılması için gerekli dosyaları içerir.

## 🏗️ Mimari

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Ingress       │    │   LoadBalancer  │    │   PostgreSQL    │
│   Controller    │───▶│   Service       │───▶│   Database      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │   Spring Boot   │
                       │   Application   │
                       │   (3 replicas)  │
                       └─────────────────┘
```

## 📋 Gereksinimler

- Kubernetes cluster (minikube, kind, veya cloud provider)
- kubectl CLI tool
- Docker

## 🚀 Hızlı Başlangıç

### 1. Docker Image Oluşturma

```bash
# Proje dizininde
docker build -t ogrenci-kayit:latest .
```

### 2. Kubernetes'e Deploy Etme

```bash
# k8s dizininde
chmod +x deploy.sh
./deploy.sh
```

### 3. Manuel Deploy

```bash
# Namespace oluştur
kubectl apply -f namespace.yaml

# Secrets ve ConfigMaps
kubectl apply -f secret.yaml
kubectl apply -f configmap.yaml

# PostgreSQL deploy
kubectl apply -f postgres-deployment.yaml

# Uygulama deploy
kubectl apply -f app-deployment.yaml

# Ingress deploy
kubectl apply -f ingress.yaml
```

## 🔍 Durum Kontrolü

```bash
# Tüm kaynakları görüntüle
kubectl get all -n ogrenci-kayit

# Pod loglarını görüntüle
kubectl logs -f deployment/ogrenci-kayit-app -n ogrenci-kayit

# Service durumunu kontrol et
kubectl get svc -n ogrenci-kayit
```

## 🧹 Temizlik

```bash
# Tüm kaynakları sil
kubectl delete namespace ogrenci-kayit
```

## ⚙️ Konfigürasyon

### Environment Variables

- `SPRING_PROFILES_ACTIVE`: prod
- `SPRING_DATASOURCE_URL`: PostgreSQL connection string
- `SPRING_DATASOURCE_USERNAME`: postgres
- `SPRING_DATASOURCE_PASSWORD`: Secret'tan alınır

### Resource Limits

- **Memory**: 512Mi request, 1Gi limit
- **CPU**: 250m request, 500m limit

### Health Checks

- **Liveness Probe**: `/actuator/health` (30s interval)
- **Readiness Probe**: `/actuator/health` (10s interval)

## 🔧 Troubleshooting

### Pod CrashLoopBackOff

```bash
# Pod loglarını kontrol et
kubectl logs <pod-name> -n ogrenci-kayit

# Pod detaylarını görüntüle
kubectl describe pod <pod-name> -n ogrenci-kayit
```

### Database Connection Issues

```bash
# PostgreSQL pod'una bağlan
kubectl exec -it <postgres-pod-name> -n ogrenci-kayit -- psql -U postgres -d ogrencikayit
```

### Service Not Accessible

```bash
# Service endpoint'lerini kontrol et
kubectl get endpoints -n ogrenci-kayit

# Port forward ile test et
kubectl port-forward svc/ogrenci-kayit-service 8080:80 -n ogrenci-kayit
``` 