package whatsappbroski.whatsappbroski.dto.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatGPTRequestDTO {

    private String model;
    private List<Message> messages;

    @Builder.Default
    private ResponseFormat response_format = ResponseFormat.builder().build();

    @Builder.Default
    private int temperature = 1;
    @Builder.Default
    private int max_completion_tokens = 256;
    @Builder.Default
    private int top_p = 1;
    @Builder.Default
    private int frequency_penalty = 0;
    @Builder.Default
    private int presence_penalty = 0;

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String role;
        private List<Content> content;
    }

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private String type;
        private String text;
    }

    @Data
    @Builder
    @JsonIgnoreProperties
    public static class ResponseFormat {
        @Builder.Default
        private String type = "text";
    }
}
