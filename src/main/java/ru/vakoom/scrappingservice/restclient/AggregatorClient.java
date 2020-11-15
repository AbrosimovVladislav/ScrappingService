package ru.vakoom.scrappingservice.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.scrappingservice.model.Brand;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AggregatorClient {

    @Value("${aggregator-serivce-path}")
    public String MATCHING_SERVICE_BASE_PATH;
    public static final String MATCHING_SERVICE_SEND_BRANDS_PATH = "/brands";

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<List<Brand>> receiveBrands() {
        String url = MATCHING_SERVICE_BASE_PATH + MATCHING_SERVICE_SEND_BRANDS_PATH;
        return restTemplate.exchange(url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });
    }

}
