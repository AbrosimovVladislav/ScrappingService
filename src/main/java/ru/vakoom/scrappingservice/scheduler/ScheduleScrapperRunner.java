package ru.vakoom.scrappingservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.repository.OfferRepository;
import ru.vakoom.scrappingservice.repository.SequenceOfferRefresher;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleScrapperRunner {

    private final List<Scrapper> scrappers;
    private final OfferRepository offerRepository;
    private final SequenceOfferRefresher sequenceOfferRefresher;
    private final RestTemplate restTemplate = new RestTemplate();

    public static final String MATCHING_SERVICE_BASE_PATH = "http://localhost:8081";
    public static final String MATCHING_SERVICE_RECEIVE_OFFERS_PATH = "/receiveOffers";


    //@Scheduled(cron = "* */5 * * * ?") // every 5 minutes
    @Scheduled(cron = "0 0 */3 * * *") // every 3 hours
    public void refreshOffers() {
        offerRepository.deleteAll();
        sequenceOfferRefresher.setHibernateSequenceCurrentValueToZero();
        List<Offer> offersForMatcherService = scrappers.stream()
                .map(Scrapper::fullCatalog)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        sendRequestToMatcher(offersForMatcherService);
        //ToDo invoke your ali client here
    }

    private void sendRequestToMatcher(List<Offer> offers) {
        String url = MATCHING_SERVICE_BASE_PATH + MATCHING_SERVICE_RECEIVE_OFFERS_PATH;
        restTemplate.exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(offers),
                ResponseEntity.class);
    }

}
