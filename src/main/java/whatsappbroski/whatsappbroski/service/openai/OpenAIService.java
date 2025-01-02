package whatsappbroski.whatsappbroski.service.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private final ChatClient chatClient;

    private final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    public OpenAIService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    public String getOpenAIResponse(String prompt) {

        long startTime = System.currentTimeMillis();

        String response = this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Received response from OpenAI in {} ms", duration);
        logger.debug("OpenAI response content: {}", response);

        return response;
    }
}
