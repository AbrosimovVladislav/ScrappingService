package ru.vakoom.scrappingservice.service.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.service.aspect.logging.MeasurePerformance;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MatcherClient {

    @Value("${matcher-service-path}")
    public String MATCHING_SERVICE_BASE_PATH;
    public static final String MATCHING_SERVICE_RECEIVE_OFFERS_PATH = "/receiveOffers";

    private final RestTemplate restTemplate = new RestTemplate();

    @MeasurePerformance
    public ResponseEntity<List<Offer>> sendOffers(List<Offer> offers) {
        String url = MATCHING_SERVICE_BASE_PATH + MATCHING_SERVICE_RECEIVE_OFFERS_PATH;
        return restTemplate.exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(offers),
                new ParameterizedTypeReference<>() {
                });
    }

}
