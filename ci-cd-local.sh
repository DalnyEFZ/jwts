#!/bin/bash
# CI/CD 本地演示脚本
# 用于在 K8s Master 上自动构建和部署

set -e

echo "=========================================="
echo "   JWTS CI/CD 自动化部署脚本"
echo "=========================================="

# 配置
PROJECT_DIR="/root/jwts"
NAMESPACE="jwts"
IMAGE_PREFIX="jwts"
IMAGE_TAG="v1.0"

# 服务列表
SERVICES=(
    "eureka-service"
    "gateway-service"
    "student-service"
    "teacher-service"
    "course-service"
    "student-client"
    "teacher-client"
    "course-client"
)

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_step() {
    echo -e "\n${YELLOW}[STEP] $1${NC}"
}

print_success() {
    echo -e "${GREEN}[SUCCESS] $1${NC}"
}

print_error() {
    echo -e "${RED}[ERROR] $1${NC}"
}

# 第一步：拉取最新代码
print_step "1. 拉取最新代码"
cd $PROJECT_DIR
if [ -d ".git" ]; then
    git pull origin main || git pull origin master
    print_success "代码更新完成"
else
    print_error "未找到 Git 仓库，请先配置远程仓库"
    exit 1
fi

# 第二步：Maven 构建
print_step "2. Maven 构建"
mvn clean package -DskipTests -B
print_success "Maven 构建完成"

# 第三步：构建 Docker 镜像
print_step "3. 构建 Docker 镜像"
for service in "${SERVICES[@]}"; do
    echo "  构建 $service ..."

    # 复制 JAR 文件
    if [ -f "$PROJECT_DIR/$service/target/$service.jar" ]; then
        cp $PROJECT_DIR/$service/target/$service.jar $PROJECT_DIR/jwts/$service/
    fi

    # 构建镜像
    cd $PROJECT_DIR/jwts/$service
    nerdctl --namespace k8s.io build -t $IMAGE_PREFIX/$service:$IMAGE_TAG . > /dev/null 2>&1

    if [ $? -eq 0 ]; then
        print_success "$service 镜像构建完成"
    else
        print_error "$service 镜像构建失败"
        exit 1
    fi
done

# 第四步：部署到 K8s
print_step "4. 部署到 K8s 集群"
cd $PROJECT_DIR

# 应用所有 YAML
kubectl apply -f jwts/k8s/

# 等待部署完成
echo "  等待所有 Pod 启动..."
kubectl rollout status deployment -n $NAMESPACE --timeout=300s

if [ $? -eq 0 ]; then
    print_success "所有服务部署成功"
else
    print_error "部署超时，请检查日志"
    exit 1
fi

# 第五步：验证部署
print_step "5. 验证部署状态"

echo ""
echo "=== Pod 状态 ==="
kubectl get pods -n $NAMESPACE -o wide

echo ""
echo "=== Service 状态 ==="
kubectl get svc -n $NAMESPACE

echo ""
echo "=== Eureka 注册服务 ==="
curl -s http://localhost:30001/eureka/apps | grep '<name>' | sed 's/.*<name>//' | sed 's/<\/name>.*//' | sort

# 第六步：测试 API
print_step "6. 测试 API 接口"

echo ""
echo "测试学生服务:"
curl -s 'http://localhost:30000/api/students?token=1' | head -c 100
echo ""

echo "测试教师服务:"
curl -s 'http://localhost:30000/api/teachers?token=1' | head -c 100
echo ""

echo "测试课程服务:"
curl -s 'http://localhost:30000/api/courses?token=1' | head -c 100
echo ""

echo ""
echo "=========================================="
print_success "CI/CD 部署完成！"
echo "=========================================="
echo ""
echo "访问地址："
echo "  - Eureka 控制台: http://192.168.186.10:30001"
echo "  - Gateway API: http://192.168.186.10:30000"
echo ""
echo "API 测试命令："
echo "  curl 'http://192.168.186.10:30000/api/students?token=1'"
echo "  curl 'http://192.168.186.10:30000/api/teachers?token=1'"
echo "  curl 'http://192.168.186.10:30000/api/courses?token=1'"
echo ""
