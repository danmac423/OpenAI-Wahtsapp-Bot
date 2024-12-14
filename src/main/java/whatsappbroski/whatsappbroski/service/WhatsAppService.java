package whatsappbroski.whatsappbroski.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WhatsAppService {

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String token;

    public void sendMessage(String to, String message) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(whatsappApiUrl))
                    .header("Authorization",
                            "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.format(
                            "{" +
                                "\"messaging_product\": \"whatsapp\"," +
                                "\"recipient_type\": \"individual\"," +
                                "\"to\": \"%s\"," +
                                "\"type\": \"text\"," +
                                "\"text\": {" +
                                    "\"preview_url\": false," +
                                    "\"body\": \"%s\"" +
                                "}" +
                            "}", to, message)))
                    .build();
            HttpClient http = HttpClient.newHttpClient();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
