package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.scrappingservice.model.Brand;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.scheduler.ScheduleScrapperRunner;
import ru.vakoom.scrappingservice.service.BrandRefresher;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final ScheduleScrapperRunner scheduler;
    private final BrandRefresher brandRefresher;

    @GetMapping("/testRefreshOffers")
    public ResponseEntity<List<Offer>> testRefreshOffers() {
        return scheduler.refreshOffers();
    }

    @GetMapping("/testRefreshBrands")
    public ResponseEntity<List<Brand>> testRefreshBrands() {
        return ResponseEntity.ok(brandRefresher.refreshBrands());
    }

}
