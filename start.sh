#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_PORT="${SERVER_PORT:-8080}"
FRONTEND_PORT="${FRONTEND_PORT:-4200}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
BACKEND_LOG="$ROOT_DIR/.backend.log"
FRONTEND_LOG="$ROOT_DIR/.frontend.log"

PIDS=()
CLEANED=0

cleanup() {
  if [ "$CLEANED" -eq 1 ]; then
    return
  fi
  CLEANED=1

  echo ""
  echo "Encerrando servicos..."
  for pid in "${PIDS[@]}"; do
    kill "$pid" 2>/dev/null || true
  done

  wait 2>/dev/null || true
}

trap cleanup SIGINT SIGTERM

load_nvm() {
  export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
  if [ -s "$NVM_DIR/nvm.sh" ]; then
    # shellcheck source=/dev/null
    . "$NVM_DIR/nvm.sh"
  fi
}

port_in_use() {
  ss -ltn 2>/dev/null | awk '{print $4}' | grep -q ":$1\$"
}

wait_for_port() {
  local host=$1
  local port=$2
  local label=$3
  local max=${4:-90}

  for _ in $(seq 1 "$max"); do
    if (echo >"/dev/tcp/${host}/${port}") 2>/dev/null; then
      echo "$label pronto: http://${host}:${port}"
      return 0
    fi
    sleep 1
  done

  echo "Timeout aguardando $label na porta $port."
  return 1
}

ensure_postgres() {
  if (echo >"/dev/tcp/${DB_HOST}/${DB_PORT}") 2>/dev/null; then
    echo "PostgreSQL ja acessivel em ${DB_HOST}:${DB_PORT}"
    return 0
  fi

  echo "Subindo PostgreSQL..."
  if "$ROOT_DIR/Backend/scripts/start-db.sh"; then
    return 0
  fi

  echo ""
  echo "Nao foi possivel iniciar o PostgreSQL automaticamente."
  echo "Opcoes:"
  echo "  1. Suba o banco manualmente: Backend/scripts/start-db.sh"
  echo "  2. Ou garanta PostgreSQL rodando em ${DB_HOST}:${DB_PORT}"
  return 1
}

ensure_port_free() {
  local port=$1
  local service=$2

  if port_in_use "$port"; then
    echo "Erro: porta ${port} ja esta em uso (${service})."
    echo "Libere com: fuser -k ${port}/tcp"
    echo "Ou pare tudo com: ./stop.sh"
    exit 1
  fi
}

main() {
  echo "=== Bolao Copa - start ==="

  ensure_port_free "$BACKEND_PORT" "backend"
  ensure_port_free "$FRONTEND_PORT" "frontend"
  ensure_postgres

  load_nvm
  if ! command -v npm >/dev/null 2>&1; then
    echo "Erro: npm nao encontrado. Instale Node.js/npm ou configure o nvm."
    exit 1
  fi

  if [ ! -d "$ROOT_DIR/frontend/node_modules" ]; then
    echo "Instalando dependencias do frontend..."
    (cd "$ROOT_DIR/frontend" && npm install)
  fi

  echo "Subindo backend (porta ${BACKEND_PORT})..."
  (
    cd "$ROOT_DIR/Backend"
    SERVER_PORT="$BACKEND_PORT" ./mvnw -q spring-boot:run
  ) >"$BACKEND_LOG" 2>&1 &
  PIDS+=($!)

  if ! wait_for_port "localhost" "$BACKEND_PORT" "Backend" 90; then
    echo "Ultimas linhas do log do backend:"
    tail -n 20 "$BACKEND_LOG" || true
    cleanup
    exit 1
  fi

  echo "Subindo frontend (porta ${FRONTEND_PORT})..."
  (
    cd "$ROOT_DIR/frontend"
    npm start -- --port "$FRONTEND_PORT" --host 0.0.0.0
  ) >"$FRONTEND_LOG" 2>&1 &
  PIDS+=($!)

  if ! wait_for_port "localhost" "$FRONTEND_PORT" "Frontend" 90; then
    echo "Ultimas linhas do log do frontend:"
    tail -n 20 "$FRONTEND_LOG" || true
    cleanup
    exit 1
  fi

  echo ""
  echo "App pronto para uso:"
  echo "  Frontend: http://localhost:${FRONTEND_PORT}"
  echo "  Backend:  http://localhost:${BACKEND_PORT}/api"
  echo "  Admin:    http://localhost:${FRONTEND_PORT}/admin (senha: 963852741jO761415xp)"
  echo ""
  echo "Logs:"
  echo "  $BACKEND_LOG"
  echo "  $FRONTEND_LOG"
  echo ""
  echo "Pressione Ctrl+C para encerrar."

  wait
}

main "$@"
