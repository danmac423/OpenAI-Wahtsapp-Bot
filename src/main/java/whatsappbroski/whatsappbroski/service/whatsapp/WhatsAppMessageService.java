package whatsappbroski.whatsappbroski.service.whatsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import whatsappbroski.whatsappbroski.dto.whatsapp.WebhookPayloadDTO;
import whatsappbroski.whatsappbroski.service.openai.OpenAIService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppMessageService {

    private final Logger logger = LoggerFactory.getLogger(WhatsAppMessageService.class);
    private final OpenAIService openAIService;

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String token;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public WhatsAppMessageService(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    public void handleIncomingMessage(WebhookPayloadDTO.Message message) {
        String from = message.getFrom();
        String body = message.getText().getBody();

        logger.info("Message received from WhatsApp: from={}, body={}", from, body);

        String openAIResponse = openAIService.getOpenAIResponse(body);

        sendMessage(from, openAIResponse);
    }


    private String createMessageRequestBody(String to, String message) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("messaging_product", "whatsapp");
            payload.put("recipient_type", "individual");
            payload.put("to", to);
            payload.put("type", "text");

            Map<String, Object> textContent = new HashMap<>();
            textContent.put("preview_url", false);
            textContent.put("body", message);

            payload.put("text", textContent);

            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            logger.error("Error while creating JSON request body", e);
            throw new RuntimeException("Failed to create JSON request body", e);
        }
    }

    public void sendMessage(String to, String message) {
        try {
            logger.info("Sending message to WhatsApp: to={}, message={}", to, message);

            String requestBody = createMessageRequestBody(to, message);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(whatsappApiUrl))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            handleResponse(response);
        } catch (URISyntaxException e) {
            logger.error("Invalid URI for WhatsApp API: {}", whatsappApiUrl, e);
        } catch (IOException | InterruptedException e) {
            logger.error("Invalid URI for WhatsApp API: {}", whatsappApiUrl, e);
            Thread.currentThread().interrupt();
        }
    }

    private void handleResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode >= 200 && statusCode < 300) {
            logger.info("Message successfully sent. Response: {}", responseBody);
        } else {
            logger.error("Failed to send message. Status Code: {}, Response: {}", statusCode, responseBody);
        }
    }

}
