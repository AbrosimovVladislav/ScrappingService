package ru.vakoom.scrappingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.scrappingservice.scheduler.ScheduleScrapperRunner;

@RestController
@RequiredArgsConstructor
public class ScrappingServiceApiController {

    private final ScheduleScrapperRunner scheduler;

    @GetMapping("/go")
    public void get() {
        scheduler.refreshOffers();
    }

}
