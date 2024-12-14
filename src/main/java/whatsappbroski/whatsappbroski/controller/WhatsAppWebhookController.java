package whatsappbroski.whatsappbroski.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whatsappbroski.whatsappbroski.service.WhatsAppService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WhatsAppWebhookController {

    private final WhatsAppService whatsAppService;

    @Value("${whatsapp.api.webhook.token}")
    String verifyToken;

    public WhatsAppWebhookController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        try {
            // Logowanie payload dla debugowania
            System.out.println("Otrzymano payload: " + payload);

            // Pobranie pierwszego wpisu z listy "entry"
            var entries = (List<Map<String, Object>>) payload.get("entry");
            if (entries == null || entries.isEmpty()) {
                return ResponseEntity.ok("No entries found");
            }

            // Pobranie zmian z wpisu
            var changes = (List<Map<String, Object>>) entries.get(0).get("changes");
            if (changes == null || changes.isEmpty()) {
                return ResponseEntity.ok("No changes found");
            }

            // Pobranie wiadomości z "value"
            var value = (Map<String, Object>) changes.get(0).get("value");
            var messages = (List<Map<String, Object>>) value.get("messages");
            if (messages == null || messages.isEmpty()) {
                return ResponseEntity.ok("No messages found");
            }

            // Pobranie nadawcy i treści wiadomości
            var message = messages.get(0);
            String from = (String) message.get("from"); // Numer telefonu nadawcy
            String body = (String) ((Map<String, Object>) message.get("text")).get("body"); // Treść wiadomości

            System.out.println("Wiadomość od: " + from + ", Treść: " + body);

            // Wysłanie odpowiedzi: odsyłamy tę samą wiadomość
            whatsAppService.sendMessage(from, "Otrzymałem Twoją wiadomość: " + body);

        } catch (Exception e) {
            e.printStackTrace();
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
}
