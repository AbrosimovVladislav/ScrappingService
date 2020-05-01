package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.shopscrapper.HockeyBezGranizScrapper;
import ru.vakoom.scrappingservice.shopscrapper.SportDepoScrapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final HockeyBezGranizScrapper hockeyBezGranizScrapper;
    private final SportDepoScrapper sportDepoScrapper;

    @GetMapping("/go")
    public List<Offer> get() {
        var a = hockeyBezGranizScrapper.fullCatalog();
//        var a = sportDepoScrapper.fullCatalog();
        return a;
    }

}
