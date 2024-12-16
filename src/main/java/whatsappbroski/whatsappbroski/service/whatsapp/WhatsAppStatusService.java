package whatsappbroski.whatsappbroski.service.whatsapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import whatsappbroski.whatsappbroski.dto.whatsapp.WebhookPayloadDTO;

@Service
public class WhatsAppStatusService {

    private final Logger logger = LoggerFactory.getLogger(WhatsAppStatusService.class);

    public void handleMessageStatus(WebhookPayloadDTO.Status status) {
        logger.info("Message status updated: ID={}, status={}, recipient={}",
                status.getId(), status.getStatus(), status.getRecipientId());
    }
}
