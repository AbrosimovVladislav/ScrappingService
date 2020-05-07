package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.scheduler.ScheduleScrapperRunner;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final ScheduleScrapperRunner scheduler;

    @GetMapping("/testRefreshOffers")
    public ResponseEntity<List<Offer>> get() {
        return scheduler.refreshOffers();
    }

}
