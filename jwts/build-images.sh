#!/usr/bin/env bash
set -euo pipefail

services=(
  eureka-service
  gateway-service
  student-service
  student-client
  teacher-service
  teacher-client
  course-service
  course-client
)

root_dir="$(cd "$(dirname "$0")" && pwd)"

for service in "${services[@]}"; do
  echo "Building jwts/${service}:v1.0"
  nerdctl -n k8s.io build \
    -t "jwts/${service}:v1.0" \
    "${root_dir}/${service}"
done

nerdctl -n k8s.io images | grep '^jwts/'
