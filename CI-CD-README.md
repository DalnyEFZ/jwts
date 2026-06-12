# CI/CD 流程说明

## 📋 概述

本项目使用 GitHub Actions 实现完整的 CI/CD 流程：
1. 代码推送到 GitHub → 自动构建 Docker 镜像
2. 镜像推送到 GitHub Container Registry (ghcr.io)
3. 在虚拟机中拉取新镜像并更新 K8s 服务

---

## 🔧 前置准备

### 1. 创建 GitHub Personal Access Token

1. 打开 GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. 点击 "Generate new token"
3. 勾选以下权限：
   - `repo` (完整仓库访问权限)
   - `read:packages` (读取包权限)
   - `write:packages` (写入包权限)
4. 生成 token 并保存

### 2. 在虚拟机中登录 GitHub Container Registry

```bash
# 登录 ghcr.io
echo "YOUR_GITHUB_TOKEN" | docker login ghcr.io -u DalnyEFZ --password-stdin
```

---

## 🚀 使用流程

### 步骤 1：推送代码到 GitHub

```bash
# 在本地修改代码后
git add .
git commit -m "your commit message"
git push origin master
```

### 步骤 2：GitHub Actions 自动构建

1. 打开 GitHub 仓库页面
2. 点击 "Actions" 标签
3. 查看构建状态（应该显示绿色 ✅）
4. 构建完成后，镜像会自动推送到 ghcr.io

### 步骤 3：在虚拟机中拉取新镜像并部署

```bash
# SSH 到 master 节点
ssh root@192.168.186.10

# 执行部署脚本
bash /path/to/deploy.sh
```

或者手动执行：

```bash
# 登录 ghcr.io
echo "YOUR_GITHUB_TOKEN" | docker login ghcr.io -u DalnyEFZ --password-stdin

# 拉取最新镜像
docker pull ghcr.io/dalnyefz/jwts/eureka-service:latest
docker pull ghcr.io/dalnyefz/jwts/student-service:latest
docker pull ghcr.io/dalnyefz/jwts/teacher-service:latest
docker pull ghcr.io/dalnyefz/jwts/course-service:latest
docker pull ghcr.io/dalnyefz/jwts/student-client:latest
docker pull ghcr.io/dalnyefz/jwts/teacher-client:latest
docker pull ghcr.io/dalnyefz/jwts/course-client:latest
docker pull ghcr.io/dalnyefz/jwts/gateway-service:latest

# 更新 K8s Deployment
kubectl set image deployment/eureka-service eureka-service=ghcr.io/dalnyefz/jwts/eureka-service:latest -n jwts
kubectl set image deployment/student-service student-service=ghcr.io/dalnyefz/jwts/student-service:latest -n jwts
kubectl set image deployment/teacher-service teacher-service=ghcr.io/dalnyefz/jwts/teacher-service:latest -n jwts
kubectl set image deployment/course-service course-service=ghcr.io/dalnyefz/jwts/course-service:latest -n jwts
kubectl set image deployment/student-client student-client=ghcr.io/dalnyefz/jwts/student-client:latest -n jwts
kubectl set image deployment/teacher-client teacher-client=ghcr.io/dalnyefz/jwts/teacher-client:latest -n jwts
kubectl set image deployment/course-client course-client=ghcr.io/dalnyefz/jwts/course-client:latest -n jwts
kubectl set image deployment/gateway-service gateway-service=ghcr.io/dalnyefz/jwts/gateway-service:latest -n jwts

# 等待更新完成
kubectl rollout status deployment -n jwts --timeout=300s

# 检查服务状态
kubectl get pods -n jwts
```

---

## 📊 流程图

```
┌─────────────────┐
│  代码推送到 GitHub  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  GitHub Actions  │
│  自动构建镜像    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  推送镜像到      │
│  ghcr.io        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  在虚拟机中      │
│  拉取新镜像      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  更新 K8s        │
│  Deployment     │
└─────────────────┘
```

---

## 🔍 验证 CI/CD

### 1. 检查 GitHub Actions

1. 打开 GitHub 仓库 → Actions
2. 查看最近的构建记录
3. 确认状态为 ✅ Success

### 2. 检查镜像是否推送成功

1. 打开 GitHub 仓库 → Packages
2. 应该可以看到 8 个镜像包

### 3. 检查虚拟机中的镜像

```bash
# 查看本地镜像
docker images | grep ghcr.io
```

### 4. 检查 K8s 服务

```bash
# 查看 Pod 状态
kubectl get pods -n jwts

# 查看使用的镜像
kubectl get pods -n jwts -o jsonpath='{.items[*].spec.containers[*].image}'
```

---

## ❓ 常见问题

### Q1：GitHub Actions 构建失败
**原因：** Maven 编译错误或测试失败
**解决：** 查看 Actions 日志，修复代码后重新推送

### Q2：无法登录 ghcr.io
**原因：** Token 权限不足或已过期
**解决：** 重新生成 Token，确保勾选 `read:packages` 和 `write:packages`

### Q3：拉取镜像失败
**原因：** 未登录或镜像不存在
**解决：** 先登录 ghcr.io，然后检查镜像是否存在

### Q4：更新 Deployment 失败
**原因：** 镜像名称错误或 K8s 连接问题
**解决：** 检查镜像名称和 K8s 集群状态

---

## 📝 演示步骤

### 演示 CI/CD 流程

1. **展示 GitHub Actions 配置**
   ```bash
   cat .github/workflows/ci-cd.yml
   ```

2. **展示 GitHub 仓库的 Actions 页面**
   - 打开浏览器访问 https://github.com/DalnyEFZ/jwts/actions
   - 显示最近的构建记录

3. **展示 GitHub 仓库的 Packages 页面**
   - 打开浏览器访问 https://github.com/DalnyEFZ?tab=packages
   - 显示 8 个镜像包

4. **在虚拟机中拉取新镜像**
   ```bash
   docker pull ghcr.io/dalnyefz/jwts/eureka-service:latest
   ```

5. **更新 K8s Deployment**
   ```bash
   kubectl set image deployment/eureka-service eureka-service=ghcr.io/dalnyefz/jwts/eureka-service:latest -n jwts
   ```

6. **验证更新成功**
   ```bash
   kubectl get pods -n jwts
   ```

---

## 🎯 总结

完整的 CI/CD 流程：
1. ✅ 代码推送到 GitHub
2. ✅ GitHub Actions 自动构建 Docker 镜像
3. ✅ 镜像推送到 ghcr.io
4. ✅ 在虚拟机中拉取新镜像
5. ✅ 更新 K8s Deployment
6. ✅ 服务自动滚动更新

**实现了代码更新后自动构建镜像并部署到 K8s 集群！** 🎉
