#!/bin/bash

# CI/CD 部署脚本
# 用于从 GitHub Container Registry 拉取新镜像并更新 K8s 服务

set -e

echo "=========================================="
echo "  CI/CD 自动部署脚本"
echo "=========================================="

# 配置信息
REGISTRY="ghcr.io"
IMAGE_NAME="dalnyefz/jwts"
NAMESPACE="jwts"

# 服务列表
SERVICES="eureka-service student-service teacher-service course-service student-client teacher-client course-client gateway-service"

echo ""
echo "【1】登录 GitHub Container Registry"
echo "请输入 GitHub Personal Access Token (需要 read:packages 权限):"
read -s GITHUB_TOKEN

echo "$GITHUB_TOKEN" | docker login $REGISTRY -u dalnyefz --password-stdin

echo ""
echo "【2】拉取最新镜像"
for SERVICE in $SERVICES; do
    echo "拉取 $SERVICE..."
    docker pull $REGISTRY/$IMAGE_NAME/$SERVICE:latest
done

echo ""
echo "【3】更新 K8s Deployment"
for SERVICE in $SERVICES; do
    echo "更新 $SERVICE..."
    kubectl set image deployment/$SERVICE $SERVICE=$REGISTRY/$IMAGE_NAME/$SERVICE:latest -n $NAMESPACE
done

echo ""
echo "【4】等待更新完成"
kubectl rollout status deployment -n $NAMESPACE --timeout=300s

echo ""
echo "【5】检查服务状态"
kubectl get pods -n $NAMESPACE

echo ""
echo "=========================================="
echo "  部署完成！"
echo "=========================================="
