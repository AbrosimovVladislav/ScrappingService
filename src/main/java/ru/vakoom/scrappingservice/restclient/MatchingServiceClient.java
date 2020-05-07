package ru.vakoom.scrappingservice.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.scrappingservice.model.Offer;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchingServiceClient {

    public static final String MATCHING_SERVICE_BASE_PATH = "http://localhost:8081";
    public static final String MATCHING_SERVICE_RECEIVE_OFFERS_PATH = "/receiveOffers";

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<List<Offer>> sendOffers(List<Offer> offers) {
        String url = MATCHING_SERVICE_BASE_PATH + MATCHING_SERVICE_RECEIVE_OFFERS_PATH;
        return restTemplate.exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(offers),
                new ParameterizedTypeReference<>() {
                });
    }

}
