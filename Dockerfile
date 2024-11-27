FROM openjdk:17

WORKDIR /app

ARG GITHUB_TOKEN

COPY scripts/download_artifact.sh run.sh

CMD sh run.sh $GITHUB_TOKEN

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]