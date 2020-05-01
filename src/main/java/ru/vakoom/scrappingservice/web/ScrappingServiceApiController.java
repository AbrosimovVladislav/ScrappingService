package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.service.HockeyBezGranizScrapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final HockeyBezGranizScrapper scrapper;

    @GetMapping("/go")
    public List<Offer> get() {
        var a = scrapper.fullCatalog();
        return a;
    }

}
