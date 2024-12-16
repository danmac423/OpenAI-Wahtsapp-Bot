package whatsappbroski.whatsappbroski.dto.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatGPTResponseDTO {

    private String id;
    private String object;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String system_fingerprint;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private int index;
        private Message message;
        private String logprobs;
        private String finish_reason;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String usage;
        private String content;
        private String refusal;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
        private PromptTokensDetails prompt_tokens_details;
        private CompletionTokensDetails completion_tokens_details;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromptTokensDetails {
        private int cached_tokens;
        private int audio_tokens;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompletionTokensDetails {
        private int reasoning_tokens;
        private int audio_tokens;
        private int accepted_prediction_tokens;
        private int rejected_prediction_tokens;
    }


}
