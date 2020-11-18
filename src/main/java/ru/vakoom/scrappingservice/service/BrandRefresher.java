package ru.vakoom.scrappingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Brand;
import ru.vakoom.scrappingservice.repository.BrandRepository;
import ru.vakoom.scrappingservice.service.restclient.AggregatorClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandRefresher {

    private final AggregatorClient aggregatorClient;
    private final BrandRepository brandRepository;

    public List<Brand> refreshBrands() {
        return Optional.ofNullable(aggregatorClient.receiveBrands().getBody())
                .map(brandRepository::saveAll)
                .orElse(Collections.emptyList());
    }

}
