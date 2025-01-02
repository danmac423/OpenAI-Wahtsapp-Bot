package whatsappbroski.whatsappbroski.dto.openai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenAIRequestDTO {
    private String userId;
    private String prompt;
}