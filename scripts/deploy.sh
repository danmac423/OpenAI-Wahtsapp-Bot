#!/bin/bash
openai.api.model=${OPENAI_API_MODEL}
openai.api.url=${OPENAI_API_URL}
openai.api.key=${OPENAI_API_KEY}
if [ $# -ne 8 ]; then
      echo "Usage: $0 <docker-username> <docker-token> <whatsapp-api-ul> <whatsapp-api-token> <whatsapp-api-webhook-token> <openai-api-model> <openai-api-url> <openai-api-key>"
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
OPENAI_API_MODEL=$6
OPENAI_API_URL=$7
OPENAI_API_KEY=$8

set -ex

echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin docker.io
docker pull msj102/whatsappbroski:latest
docker run -d  \
  -e WHATSAPP_API_URL="${WHATSAPP_API_URL}" \
  -e WHATSAPP_API_TOKEN="${WHATSAPP_API_TOKEN}" \
  -e WHATSAPP_API_WEBHOOK_TOKEN="${WHATSAPP_API_WEBHOOK_TOKEN}" \
  -e OPENAI_API_MODEL="${OPENAI_API_MODEL}" \
  -e OPENAI_API_URL="${OPENAI_API_URL}" \
  -e OPENAI_API_KEY="${OPENAI_API_KEY}" \
  -p "8080:8080" --name $CONTAINER_NAME --rm msj102/whatsappbroski
