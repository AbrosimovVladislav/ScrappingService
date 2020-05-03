package ru.vakoom.scrappingservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.repository.OfferRepository;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final List<Scrapper> scrappers;
    private final OfferRepository offerRepository;

    //@Scheduled(cron = "* */5 * * * ?") // every 5 minutes
    @Scheduled(cron = "* * */3 * * ?") // every 3 hours
    public List<Offer> refreshOffers() {
        offerRepository.deleteAll();
        return scrappers.stream()
                .map(Scrapper::fullCatalog)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

}
