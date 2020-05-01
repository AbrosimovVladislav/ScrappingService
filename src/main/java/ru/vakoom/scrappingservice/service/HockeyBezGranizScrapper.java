package ru.vakoom.scrappingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.repository.HockeyRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//    https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/
@Slf4j
@Service
public class HockeyBezGranizScrapper extends Scrapper {

    //TODO ДОКОСТЫЛИТЬ С ДЕТСКИМИ

    @Autowired
    private HockeyRepository hockeyRepository;

    @Override
    public List<Offer> fullCatalog() { //TODO move to scrapper
        /*, "/detskaya-ekipirovka/"*/
        List<String> catalogUrls = new ArrayList<>(scrapperMeta.getMenuItems());
        return catalogUrls.stream()
                .map(this::menuItem)
                .flatMap(List::stream)
                .peek(hockeyRepository::saveOrUpdate)
                .collect(Collectors.toList());
    }

    @Override
    public List<Offer> menuItem(String menuItemUrl) {
        String menuItemFullPath = scrapperMeta.getBasePath() + menuItemUrl;
        Document fullMenuItemDOc;
        try {
            fullMenuItemDOc = Jsoup.connect(menuItemFullPath).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return scrapperService.getElementValueByClassAndAttribute(fullMenuItemDOc, "category-more")
                .stream()
                .map(catalogWithMenuItemWithCategoryPath -> category(scrapperMeta.getBasePath() + catalogWithMenuItemWithCategoryPath))
                .flatMap(List::stream)
                .collect(Collectors.toList());
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
