package ru.vakoom.scrappingservice.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.repository.OfferRepository;
import ru.vakoom.scrappingservice.repository.SequenceOfferRefresher;
import ru.vakoom.scrappingservice.restclient.MatcherClient;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleScrapperRunner {

    private final List<Scrapper> scrappers;
    private final OfferRepository offerRepository;
    private final SequenceOfferRefresher sequenceOfferRefresher;
    private final MatcherClient matcherClient;

    //@Scheduled(cron = "* */5 * * * ?") // every 5 minutes
    @Scheduled(cron = "0 0 */3 * * *") // every 3 hours
    public ResponseEntity<List<Offer>> refreshOffers() {
        offerRepository.deleteAll();
        sequenceOfferRefresher.setHibernateSequenceCurrentValueToZero();
        List<Offer> offersForMatcherService = scrappers.parallelStream()
                .map(Scrapper::fullCatalog)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return matcherClient.sendOffers(offersForMatcherService);
    }

}

