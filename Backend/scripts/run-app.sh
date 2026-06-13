#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PORT="${SERVER_PORT:-8080}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

echo "Verificando porta ${PORT}..."
if ss -ltn | awk '{print $4}' | grep -q ":${PORT}$"; then
  echo "Erro: a porta ${PORT} ja esta em uso."
  echo "Pare a instancia anterior com:"
  echo "  fuser -k ${PORT}/tcp"
  echo "Ou use outra porta:"
  echo "  SERVER_PORT=8081 ./scripts/run-app.sh"
  exit 1
fi

echo "Verificando PostgreSQL em ${DB_HOST}:${DB_PORT}..."
if ! (echo >"/dev/tcp/${DB_HOST}/${DB_PORT}") 2>/dev/null; then
  echo "Erro: PostgreSQL nao esta acessivel em ${DB_HOST}:${DB_PORT}."
  echo "Suba o banco primeiro:"
  echo "  ./scripts/start-db.sh"
  exit 1
fi

cd "$ROOT_DIR"
exec ./mvnw spring-boot:run
