package whatsappbroski.whatsappbroski.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "Hello World! I'm Broski, what's up???";
    }
}
