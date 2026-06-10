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

output_dir="${1:-jwts-images}"
mkdir -p "${output_dir}"

for service in "${services[@]}"; do
  nerdctl -n k8s.io save \
    -o "${output_dir}/${service}-v1.0.tar" \
    "jwts/${service}:v1.0"
done
