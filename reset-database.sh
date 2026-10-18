#!/bin/bash
set -e

echo "=========================================================="
echo "  Reiniciando containers Docker (Postgres + RabbitMQ)"
echo "=========================================================="
cd "$(dirname "$0")"

docker compose down -v
docker compose up -d

echo ""
echo "Containers reiniciados do zero com sucesso!"
echo "Na próxima inicialização do backend, o Liquibase carregará todas as seeds automaticamente."
echo "=========================================================="
