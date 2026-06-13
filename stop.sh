#!/usr/bin/env bash
set -euo pipefail

BACKEND_PORT="${SERVER_PORT:-8080}"
FRONTEND_PORT="${FRONTEND_PORT:-4200}"

stop_port() {
  local port=$1
  local label=$2

  if fuser "${port}/tcp" >/dev/null 2>&1; then
    echo "Parando ${label} na porta ${port}..."
    fuser -k "${port}/tcp" >/dev/null 2>&1 || true
  else
    echo "${label} nao esta rodando na porta ${port}."
  fi
}

echo "=== Bolao Copa - stop ==="
stop_port "$BACKEND_PORT" "backend"
stop_port "$FRONTEND_PORT" "frontend"
echo "Concluido."
