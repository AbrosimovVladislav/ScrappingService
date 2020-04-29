package ru.vakoom.scrappingservice.service;

import org.junit.Test;
import ru.vakoom.scrappingservice.model.Product;

import java.util.List;

public class HockeyBezGranizScrappingServiceTest {

    HockeyBezGranizScrappingService scrapper = new HockeyBezGranizScrappingService();

    @Test
    public void test() {
        List<Product> products = scrapper.productsPage();
    }
}
