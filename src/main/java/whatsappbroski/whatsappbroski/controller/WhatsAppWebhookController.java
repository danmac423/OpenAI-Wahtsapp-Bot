package whatsappbroski.whatsappbroski.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whatsappbroski.whatsappbroski.dto.whatsapp.WebhookPayloadDTO;
import whatsappbroski.whatsappbroski.service.openai.OpenAIService;
import whatsappbroski.whatsappbroski.service.whatsapp.WhatsAppMessageService;
import whatsappbroski.whatsappbroski.service.whatsapp.WhatsAppStatusService;

@RestController
@RequestMapping("/webhook")
public class WhatsAppWebhookController {

    private final WhatsAppMessageService whatsAppMessageService;
    private final WhatsAppStatusService whatsAppStatusService;

    private final OpenAIService openAIService;

    private final Logger logger = LoggerFactory.getLogger(WhatsAppWebhookController.class);

    @Value("${whatsapp.api.webhook.token}")
    String verifyToken;

    public WhatsAppWebhookController(WhatsAppMessageService whatsAppMessageService, WhatsAppStatusService whatsAppStatusService, OpenAIService  openAIService) {
        this.whatsAppMessageService = whatsAppMessageService;
        this.whatsAppStatusService = whatsAppStatusService;
        this.openAIService = openAIService;
    }

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody WebhookPayloadDTO payload) {
        try {
            logger.info("Received payload: {}", payload);

            var entries = payload.getEntry();
            if (entries == null || entries.isEmpty()) {
                return ResponseEntity.ok("No entries found");
            }

            entries.forEach(entry -> entry.getChanges().forEach(change -> {
                var value = change.getValue();

                if (value.getMessages() != null && !value.getMessages().isEmpty()) {
                    value.getMessages().forEach(whatsAppMessageService::handleIncomingMessage);
                }

                if (value.getStatuses() != null && !value.getStatuses().isEmpty()) {
                    value.getStatuses().forEach(whatsAppStatusService::handleMessageStatus);
                }
            }));
        } catch (Exception e) {
            logger.error("Error processing webhook payload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }

        return ResponseEntity.ok("EVENT_RECEIVED");
    }


    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String token) {

        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

//    @PostMapping("/message")
//    public ResponseEntity<String> sendMessageToChat(@RequestBody String message) {;
//        String openAIResponse = openAIService.getOpenAIResponse(message);
//        System.out.println(openAIResponse);
//        return ResponseEntity.ok(openAIResponse);
//    }
}
