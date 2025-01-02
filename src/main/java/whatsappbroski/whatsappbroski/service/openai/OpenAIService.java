package whatsappbroski.whatsappbroski.service.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class OpenAIService {

    @Value("${openai.memory.window.size}")
    private int memoryWindowSize;

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    private final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    public OpenAIService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory)).build();
        this.chatMemory = chatMemory;
    }

    public String getOpenAIResponse(String chatId, String prompt) {

        long startTime = System.currentTimeMillis();

        String response = this.chatClient
                .prompt()
                .user(prompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, memoryWindowSize)
                )
                .call()
                .content();
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Received response from OpenAI in {} ms", duration);
        logger.debug("OpenAI response content: {}", response);

        return response;
    }

    public void clearChatMemory(String chatId) {
        chatMemory.clear(chatId);
    }
}
