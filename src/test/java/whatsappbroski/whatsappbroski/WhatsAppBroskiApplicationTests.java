package whatsappbroski.whatsappbroski;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import whatsappbroski.whatsappbroski.controller.HelloController;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class WhatsAppBroskiApplicationTests {

    @Autowired
    private HelloController helloController;

    @Test
    void contextLoads() {
        assertThat(helloController).isNotNull();
    }

    @Test
    void mainMethodTest() {
        assertDoesNotThrow(() -> WhatsAppBroskiApplication.main(new String[] {}));
    }

}
