#!/bin/bash

if [ $# -ne 2 ]; then
      echo "Usage: $0 <docker-username> <docker-token>"
      exit 1
  fi

CONTAINER_NAME="whatsappbroski-app"

if docker ps --filter "name=${CONTAINER_NAME}" --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo "Kontener '${CONTAINER_NAME}' działa. Zatrzymuję..."
    docker stop "${CONTAINER_NAME}"
    docker rm "$CONTAINER_NAME"
    echo "Kontener '${CONTAINER_NAME}' został zatrzymany i usunięty."
else
    echo "Kontener '${CONTAINER_NAME}' nie działa."
fi

DOCKER_USERNAME=$1
DOCKER_TOKEN=$2

set -ex

docker login docker.io "$DOCKER_USERNAME" --password "$DOCKER_TOKEN"
docker pull msj102/whatsappbroski:latest
docker run -d -p "8080:8080" --name "$CONTAINER_NAME" --rm msj102/whatsappbroski
