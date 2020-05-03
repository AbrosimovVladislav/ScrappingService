package ru.vakoom.scrappingservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.repository.OfferRepository;
import ru.vakoom.scrappingservice.repository.SequenceOfferRefresher;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final List<Scrapper> scrappers;
    private final OfferRepository offerRepository;
    private final SequenceOfferRefresher sequenceOfferRefresher;

    //@Scheduled(cron = "* */5 * * * ?") // every 5 minutes
    @Scheduled(cron = "0 0 */3 * * *") // every 3 hours
    public void refreshOffers() {
        offerRepository.deleteAll();
        sequenceOfferRefresher.setHibernateSequenceCurrentValueToZero();
        List<Offer> offersForMatcherService = scrappers.stream()
                .map(Scrapper::fullCatalog)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        //ToDo invoke your ali client here
    }

}
