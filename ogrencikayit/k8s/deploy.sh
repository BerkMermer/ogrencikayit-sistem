#!/bin/bash

# Kubernetes deployment script for Ogrenci Kayit System

echo "🚀 Starting Kubernetes deployment..."

# Create namespace
echo "📦 Creating namespace..."
kubectl apply -f namespace.yaml

# Apply secrets and configmaps
echo "🔐 Applying secrets and configmaps..."
kubectl apply -f secret.yaml
kubectl apply -f configmap.yaml

# Deploy PostgreSQL
echo "🐘 Deploying PostgreSQL..."
kubectl apply -f postgres-deployment.yaml

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres -n ogrenci-kayit --timeout=300s

# Deploy application
echo "📱 Deploying Spring Boot application..."
kubectl apply -f app-deployment.yaml

# Deploy ingress
echo "🌐 Deploying ingress..."
kubectl apply -f ingress.yaml

# Wait for application to be ready
echo "⏳ Waiting for application to be ready..."
kubectl wait --for=condition=ready pod -l app=ogrenci-kayit-app -n ogrenci-kayit --timeout=300s

echo "✅ Deployment completed!"
echo "🌍 Access the application at: http://ogrenci-kayit.local"

# Show status
echo "📊 Current status:"
kubectl get all -n ogrenci-kayit 