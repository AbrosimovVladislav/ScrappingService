package ru.vakoom.scrappingservice.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import ru.vakoom.scrappingservice.model.Brand;
import ru.vakoom.scrappingservice.model.Offer;

import java.util.List;

public interface ScrappingServiceApi {

    @ApiOperation(value = "Refresh offers",
            notes = "Start the procedure of scrapping offers from shops websites",
            response = ResponseEntity.class)
    ResponseEntity<List<Offer>> testRefreshOffers();

    @ApiOperation(value = "RefreshBrands",
            notes = "Get actual brands from aggregator service",
            response = ResponseEntity.class)
    ResponseEntity<List<Brand>> testRefreshBrands();

}
