#!/bin/bash

if [ $# -ne 5 ]; then
      echo "Usage: $0 <docker-username> <docker-token> <whatsapp-api-ul> <whatsapp-api-token> <whatsapp-api-webhook-token>"
      exit 1
  fi

CONTAINER_NAME="whatsappbroski-app"

if docker ps --filter "name=${CONTAINER_NAME}" --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo "Kontener '${CONTAINER_NAME}' działa. Zatrzymuję..."
    docker stop "${CONTAINER_NAME}"
    echo "Kontener '${CONTAINER_NAME}' został zatrzymany."
else
    echo "Kontener '${CONTAINER_NAME}' nie działa."
fi

DOCKERHUB_USERNAME=$1
DOCKERHUB_TOKEN=$2
WHATSAPP_API_URL=$3
WHATSAPP_API_TOKEN=$4
WHATSAPP_API_WEBHOOK_TOKEN=$5

set -ex

echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin docker.io
docker pull msj102/whatsappbroski:latest
docker run -d  -e WHATSAPP_API_URL="${WHATSAPP_API_URL}" -e WHATSAPP_API_TOKEN="${WHATSAPP_API_TOKEN}" -e WHATSAPP_API_WEBHOOK_TOKEN="${WHATSAPP_API_WEBHOOK_TOKEN}" -p "8080:8080" --name $CONTAINER_NAME --rm msj102/whatsappbroski
