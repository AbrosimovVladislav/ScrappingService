package ru.vakoom.scrappingservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vakoom.scrappingservice.model.Product;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "redirect:index.html";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello \n" +
                "http://localhost:8080";
    }

}
