#!/usr/bin/env bash
set -euo pipefail

CONTAINER_NAME="bolao-postgres"

if docker ps -a --format '{{.Names}}' | grep -qx "$CONTAINER_NAME"; then
  docker stop "$CONTAINER_NAME"
  echo "Container $CONTAINER_NAME parado."
else
  echo "Container $CONTAINER_NAME nao existe."
fi
