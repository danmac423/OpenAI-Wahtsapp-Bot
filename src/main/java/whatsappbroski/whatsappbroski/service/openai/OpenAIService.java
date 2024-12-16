package whatsappbroski.whatsappbroski.service.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import whatsappbroski.whatsappbroski.dto.openai.ChatGPTRequest;
import whatsappbroski.whatsappbroski.dto.openai.ChatGPTResponse;
import whatsappbroski.whatsappbroski.dto.openai.Message;

import java.util.Collections;

@Service
public class OpenAIService {
    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getOpenAIResponse(String prompt) {
        Message userMessage = new Message("user", prompt);
        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(model, Collections.singletonList(userMessage));

        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(url, chatGPTRequest, ChatGPTResponse.class);

        if (chatGPTResponse != null && !chatGPTResponse.getChoices().isEmpty()) {
            return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        }

        return "Sorry, I couldn't process your request.";
    }
}
