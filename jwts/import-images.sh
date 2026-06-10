#!/usr/bin/env bash
set -euo pipefail

image_dir="${1:-jwts-images}"

for image in "${image_dir}"/*.tar; do
  nerdctl -n k8s.io load -i "${image}"
done

nerdctl -n k8s.io images | grep '^jwts/'
