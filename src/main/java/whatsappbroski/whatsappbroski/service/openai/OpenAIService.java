package whatsappbroski.whatsappbroski.service.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import whatsappbroski.whatsappbroski.dto.openai.ChatGPTRequestDTO;
import whatsappbroski.whatsappbroski.dto.openai.ChatGPTResponseDTO;
import whatsappbroski.whatsappbroski.service.whatsapp.WhatsAppMessageService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class OpenAIService {
    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Value("${openai.api.key}")
    private String apiKey;

    private final Logger logger = LoggerFactory.getLogger(WhatsAppMessageService.class);

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getOpenAIResponse(String prompt) {
        ChatGPTRequestDTO.Content content = ChatGPTRequestDTO.Content.builder()
                .type("text")
                .text(prompt)
                .build();

        ChatGPTRequestDTO.Message message = ChatGPTRequestDTO.Message.builder()
                .role("user")
                .content(List.of(content))
                .build();

        ChatGPTRequestDTO chatGPTRequest = ChatGPTRequestDTO.builder()
                .model(model)
                .messages(List.of(message))
                .build();

        try {
            logger.info("Preparing request to OpenAI API. URL: {}", url);

            String requestBody = objectMapper.writeValueAsString(chatGPTRequest);
            logger.debug("Request body: {}", requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            long startTime = System.currentTimeMillis();
            logger.info("Sending request to OpenAI...");
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long duration = System.currentTimeMillis() - startTime;

            logger.info("Received response from OpenAI in {} ms", duration);
            logger.debug("Response body: {}", response.body());

            ChatGPTResponseDTO chatGPTResponse = objectMapper.readValue(response.body(), ChatGPTResponseDTO.class);

            String result = chatGPTResponse.getChoices().getFirst().getMessage().getContent();
            logger.info("OpenAI response content: {}", result);

            return result;
        } catch (Exception e) {
            logger.error("Error while communicating with OpenAI", e);
            throw new RuntimeException("Error while communicating with OpenAI", e);
        }
    }
}
