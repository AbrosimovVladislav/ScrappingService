package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.scrappingservice.model.Product;
import ru.vakoom.scrappingservice.service.HockeyBezGranizScrappingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final HockeyBezGranizScrappingService scrapper;

    @GetMapping("/productScrapping")
    public List<Product> get() {
        var a = scrapper.catalog();
        return a;
    }

}
