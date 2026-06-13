#!/usr/bin/env bash
set -euo pipefail

CONTAINER_NAME="bolao-postgres"
VOLUME_NAME="bolao_postgres_data"
IMAGE="postgres:16-alpine"

if docker ps --format '{{.Names}}' | grep -qx "$CONTAINER_NAME"; then
  echo "PostgreSQL ja esta rodando ($CONTAINER_NAME)"
  exit 0
fi

if docker ps -a --format '{{.Names}}' | grep -qx "$CONTAINER_NAME"; then
  echo "Iniciando container existente..."
  docker start "$CONTAINER_NAME"
else
  echo "Criando container PostgreSQL..."
  docker run -d \
    --name "$CONTAINER_NAME" \
    --restart unless-stopped \
    -e POSTGRES_DB=bolao \
    -e POSTGRES_USER=bolao \
    -e POSTGRES_PASSWORD=bolao \
    -p 5432:5432 \
    -v "${VOLUME_NAME}:/var/lib/postgresql/data" \
    "$IMAGE"
fi

echo "Aguardando PostgreSQL ficar pronto..."
for _ in $(seq 1 30); do
  if docker exec "$CONTAINER_NAME" pg_isready -U bolao -d bolao >/dev/null 2>&1; then
    echo "PostgreSQL pronto em localhost:5432 (db=bolao, user=bolao, pass=bolao)"
    exit 0
  fi
  sleep 1
done

echo "PostgreSQL demorou para iniciar. Verifique com: docker logs $CONTAINER_NAME"
exit 1
