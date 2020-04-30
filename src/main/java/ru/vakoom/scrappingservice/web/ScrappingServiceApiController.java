package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vakoom.scrappingservice.model.Product;
import ru.vakoom.scrappingservice.service.HockeyBezGranizScrappingService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final HockeyBezGranizScrappingService scrapper;

    @GetMapping(path = "/productScrapping")
    public List<Product> get() {
        var a = scrapper.category();
        return a;
    }

}
