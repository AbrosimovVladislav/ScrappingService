package ru.vakoom.scrappingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Product;
import ru.vakoom.scrappingservice.repository.HockeyRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//    https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/
@Slf4j
@Service
public class HockeyBezGranizScrappingService extends Scrapper {

    //TODO ДОБАВИТЬ ИНФУ ПРО НАЛИЧИЕ В МАГАЗИНЕ. ЕСЛИ ПРЕДЗАКАЗ, ЗНАЧИТ НЕТ В НАЛИЧИИ
    //TODO ДОКОСТЫЛИТЬ С ДЕТСКИМИ

    @Autowired
    private HockeyRepository hockeyRepository;

    @Override
    public List<Product> fullCatalog() { //TODO move to scrapper
        List<String> catalogUrls = List.of(/*"/catalog/konki", "/catalog/zashchita-igroka", "/catalog/klyushki", "/catalog/odezhda", "/catalog/vratar", "/catalog/sumki", "/catalog/aksessuary", "/catalog/trenazhery",*/ "/catalog/raznoe", "/detskaya-ekipirovka/");
        return catalogUrls.stream()
                .map(this::menuItem)
                .flatMap(List::stream)
                .peek(hockeyRepository::saveOrUpdate)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> menuItem(String menuItemUrl) {
        String menuItemFullPath = scrapperMeta.getBasePath() + menuItemUrl;
        Document fullMenuItemDOc = null;
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
