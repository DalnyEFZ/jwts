# CI/CD 配置指南

## 概述

本项目使用 GitHub Actions 实现 CI/CD 自动化流程：
- **持续集成 (CI)**：自动构建、测试
- **持续部署 (CD)**：自动部署到 K8s 集群

## 第一步：创建 GitHub 仓库

1. 登录 GitHub，点击右上角 "+" → "New repository"
2. 填写仓库信息：
   - Repository name: `jwts` 或 `cloud-native-lab`
   - 选择 Public 或 Private
   - 不要初始化 README（我们已有代码）
3. 点击 "Create repository"

## 第二步：推送代码到 GitHub

```bash
# 在 jwts 项目目录下
cd /d/cloudnativelab/jwts

# 初始化 Git 仓库（如果还没有）
git init

# 添加远程仓库（替换 YOUR_USERNAME 和 YOUR_REPO）
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit: JWTS microservices with CI/CD"

# 推送
git push -u origin main
```

## 第三步：配置 GitHub Secrets

在 GitHub 仓库页面：Settings → Secrets and variables → Actions → New repository secret

需要添加以下 Secrets：

| Secret 名称 | 说明 | 示例 |
|-------------|------|------|
| `DOCKER_USERNAME` | Docker Hub 用户名 | `yourusername` |
| `DOCKER_PASSWORD` | Docker Hub 密码或 Access Token | `********` |
| `K8S_HOST` | K8s Master 节点 IP | `192.168.186.10` |
| `K8S_USERNAME` | K8s SSH 用户名 | `root` |
| `K8S_SSH_KEY` | K8s SSH 私钥 | `-----BEGIN OPENSSH PRIVATE KEY-----...` |

### 获取 SSH 私钥

```bash
# 在 Windows 上查看私钥
cat ~/.ssh/id_ed25519

# 或者
cat ~/.ssh/id_rsa
```

复制整个内容（包括 BEGIN 和 END 行）粘贴到 `K8S_SSH_KEY` Secret 中。

## 第四步：创建 Docker Hub 仓库（可选）

如果想使用 Docker Hub 存储镜像：

1. 注册 [Docker Hub](https://hub.docker.com/)
2. 创建 Access Token：
   - 登录 → Account Settings → Security → New Access Token
   - 复制 token 到 GitHub Secrets 的 `DOCKER_PASSWORD`

## 第五步：触发 CI/CD

### 自动触发
- 推送代码到 `main` 或 `master` 分支
- 创建 Pull Request

### 手动触发
1. 在 GitHub 仓库页面，点击 "Actions" 标签
2. 选择 "JWTS CI/CD Pipeline"
3. 点击 "Run workflow"

## 查看 CI/CD 状态

1. 在 GitHub 仓库页面，点击 "Actions" 标签
2. 可以看到所有工作流运行记录
3. 点击具体的运行可以查看详细日志

## CI/CD 流程图

```
代码推送 → GitHub Actions 触发
         ↓
    构建并测试 (Maven)
         ↓
    构建 Docker 镜像
         ↓
    推送到 Docker Hub
         ↓
    部署到 K8s 集群
         ↓
    验证部署状态
```

## 简化版本（不使用 Docker Hub）

如果不想配置 Docker Hub，可以修改工作流，直接在 K8s Master 上构建镜像：

```yaml
# 在 deploy 任务中直接构建
- name: Build and deploy on K8s
  run: |
    ssh ${{ secrets.K8S_USERNAME }}@${{ secrets.K8S_HOST }} << 'EOF'
      cd /root/jwts
      git pull
      
      # 构建所有服务
      mvn clean package -DskipTests
      
      # 构建并加载镜像
      for service in eureka-service gateway-service student-service teacher-service course-service student-client teacher-client course-client; do
        cp ${service}/target/${service}.jar jwts/${service}/
        cd jwts/${service}
        nerdctl --namespace k8s.io build -t jwts/${service}:v1.0 .
        cd ../..
      done
      
      # 重启部署
      kubectl rollout restart deployment -n jwts
    EOF
```

## 故障排查

### 1. 构建失败
- 检查 Maven 依赖是否正确
- 查看 GitHub Actions 日志

### 2. 镜像推送失败
- 检查 Docker Hub 凭据是否正确
- 确认 Docker Hub 仓库存在

### 3. 部署失败
- 检查 SSH 密钥是否正确
- 确认 K8s 集群可访问
- 查看 K8s 日志：`kubectl logs -n jwts <pod-name>`

## 更多资源

- [GitHub Actions 文档](https://docs.github.com/en/actions)
- [Docker Hub 文档](https://docs.docker.com/docker-hub/)
- [Kubernetes 文档](https://kubernetes.io/docs/)
