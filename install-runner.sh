#!/bin/bash

# GitHub Actions Runner 安装脚本
# 用于在虚拟机中安装 self-hosted runner

set -e

echo "=========================================="
echo "  安装 GitHub Actions Runner"
echo "=========================================="

# 配置信息
REPO_URL="https://github.com/DalnyEFZ/jwts"
RUNNER_NAME="k8s-master"
RUNNER_LABELS="self-hosted,linux,x64,k8s"
RUNNER_USER="github-runner"

# 获取 GitHub Token
echo ""
echo "请输入 GitHub Personal Access Token:"
echo "(需要 repo 和 admin:org 权限)"
read -s GITHUB_TOKEN

if [ -z "$GITHUB_TOKEN" ]; then
    echo "ERROR: Token 不能为空"
    exit 1
fi

# 创建用户（如果不存在）
echo ""
echo "【1】创建 runner 用户"
if ! id "$RUNNER_USER" &>/dev/null; then
    useradd -m -s /bin/bash $RUNNER_USER
    echo "用户 $RUNNER_USER 创建成功"
else
    echo "用户 $RUNNER_USER 已存在"
fi

# 创建目录
echo ""
echo "【2】创建 runner 目录"
mkdir -p /opt/actions-runner
cd /opt/actions-runner

# 下载 runner
echo ""
echo "【3】下载 GitHub Actions Runner"
if [ ! -f "actions-runner-linux-x64-2.311.0.tar.gz" ]; then
    curl -o actions-runner-linux-x64-2.311.0.tar.gz -L https://github.com/actions/runner/releases/download/v2.311.0/actions-runner-linux-x64-2.311.0.tar.gz
else
    echo "Runner 已下载，跳过..."
fi

# 解压
echo ""
echo "【4】解压 runner"
tar xzf ./actions-runner-linux-x64-2.311.0.tar.gz

# 修改目录权限
echo ""
echo "【5】修改目录权限"
chown -R $RUNNER_USER:$RUNNER_USER /opt/actions-runner

# 配置 runner（使用普通用户）
echo ""
echo "【6】配置 runner"
su - $RUNNER_USER -c "cd /opt/actions-runner && ./config.sh --url $REPO_URL --token $GITHUB_TOKEN --name $RUNNER_NAME --labels $RUNNER_LABELS --unattended"

# 安装服务
echo ""
echo "【7】安装 runner 服务"
./svc.sh install $RUNNER_USER

# 启动服务
echo ""
echo "【8】启动 runner 服务"
./svc.sh start

echo ""
echo "=========================================="
echo "  安装完成！"
echo "=========================================="
echo ""
echo "Runner 已启动，等待 GitHub Actions 任务..."
echo ""
echo "查看 runner 状态："
echo "  cd /opt/actions-runner && ./svc.sh status"
echo ""
echo "停止 runner："
echo "  cd /opt/actions-runner && ./svc.sh stop"
echo ""
echo "卸载 runner："
echo "  cd /opt/actions-runner && ./svc.sh uninstall"
