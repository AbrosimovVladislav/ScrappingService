package ru.vakoom.scrappingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Brand;
import ru.vakoom.scrappingservice.repository.BrandRepository;
import ru.vakoom.scrappingservice.restclient.AggregatorServiceClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandRefresher {

    private final AggregatorServiceClient aggregatorServiceClient;
    private final BrandRepository brandRepository;

    public List<Brand> refreshBrands() {
        return Optional.ofNullable(aggregatorServiceClient.receiveBrands().getBody())
                .map(brandRepository::saveAll)
                .orElse(Collections.emptyList());
    }

}
