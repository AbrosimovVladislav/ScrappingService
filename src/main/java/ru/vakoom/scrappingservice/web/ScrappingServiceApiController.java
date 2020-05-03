package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.scheduler.Scheduler;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final Scheduler scheduler;

    @GetMapping("/go")
    public List<Offer> get() {
        List<Offer> a = scheduler.refreshOffers();
        return a;
    }

}
