package ru.vakoom.scrappingservice.shopscrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;
import ru.vakoom.scrappingservice.scrappersystem.ScrapperMeta;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//    https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/
@Slf4j
@Service
public class HockeyBezGranizScrapper extends Scrapper {
    @Override
    @PostConstruct
    public void init() {
        scrapperMeta = ScrapperMeta.fromJson("src/main/resources/web-shop-config/hockeybezgranic.json");
    }

    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        int lastPageNumber = 1;
        Elements pageNav = fullCategoryDoc.getElementsByClass("page-nav");
        if (pageNav.isEmpty()) return ++lastPageNumber;
        for (Element hrefE : pageNav.get(0).getElementsByAttribute("href")) {
            int currentPageNumber = Integer.parseInt(hrefE.text());
            if (lastPageNumber < currentPageNumber) {
                lastPageNumber = currentPageNumber;
            }
        }
        return ++lastPageNumber;
    }
}
