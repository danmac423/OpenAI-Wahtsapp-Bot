# WhatsApp Broski

## Description

WhatsApp Broski is a Spring Boot application that powers a WhatsApp chatbot using the OpenAI API. It's designed to be deployed in a Docker container and can be integrated with WhatsApp using webhooks. The application can engage in conversations, remember the chat history for each user, and has a CI/CD pipeline set up with GitLab for automated building, testing, and deployment.

## Features

-   **WhatsApp Integration**: Receives and responds to WhatsApp messages in real-time via webhooks.
-   **AI-Powered Conversations**: Uses OpenAI's models to generate intelligent and context-aware responses.
-   **Conversation Memory**: Maintains a history of the conversation with each user to provide more contextually relevant answers.
-   **Clear Chat History**: A user can clear their conversation history by sending the `/clear` command.
-   **Dockerized Deployment**: The entire application is containerized for easy deployment and scalability.
-   **CI/CD Pipeline**: A complete GitLab CI/CD pipeline is set up to automate the build, testing, publishing, and deployment process.

## Getting Started

### Prerequisites

-   Java 21
-   Maven
-   Docker

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://gitlab.com/whatsapp-broski-group/whatsapp-broski.git
    cd whatsapp-broski
    ```

2.  **Set up environment variables:**
    The application is configured using environment variables. You can use a `.env` file with `spring-dotenv` or set them directly in your environment. These are necessary for both local development and deployment.
    ```
    WHATSAPP_API_URL=${WHATSAPP_API_URL}
    WHATSAPP_API_TOKEN=${WHATSAPP_API_TOKEN}
    WHATSAPP_API_WEBHOOK_TOKEN=${WHATSAPP_API_WEBHOOK_TOKEN}
    OPENAI_API_KEY=${OPENAI_API_KEY}
    OPENAI_API_MODEL=${OPENAI_API_MODEL}
    ```

3.  **Build and run the application locally:**
    You can use the provided `start.sh` script to build and run the application locally:
    ```bash
    chmod +x scripts/start.sh
    ./scripts/start.sh
    ```
    This script will check for the required dependencies, build the project, and run the application JAR file.

## Deployment

The application is designed to be deployed using Docker. The `.gitlab-ci.yml` file contains the complete CI/CD pipeline for deploying the application.

### Docker Hub

The CI/CD pipeline builds the Docker image and pushes it to Docker Hub under the name `msj102/whatsappbroski:latest`. You can pull the image and run it with the required environment variables as shown in the `deploy.sh` script.

### CI/CD Pipeline
The GitLab CI/CD pipeline (`.gitlab-ci.yml`) has the following stages:

- `build`: Compiles the code, runs tests, and creates a JAR artifact.
- `publish`: Publishes the JAR artifact to GitHub Packages.
- `dockerize`: Builds a Docker image and pushes it to Docker Hub.
- `deploy`: Deploys the Docker image to a virtual machine using SSH.

## API Endpoints
- `GET /`: A simple health check endpoint that returns `"Hello World! I'm Broski, what's up???"`.
- `POST /webhook`: The endpoint for receiving WhatsApp message and status update events.
- `GET /webhook`: The endpoint used by WhatsApp to verify the webhook's authenticity.

