package ru.vakoom.scrappingservice.shopscrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.model.ScrappingDateLog;
import ru.vakoom.scrappingservice.repository.OfferRepository;
import ru.vakoom.scrappingservice.repository.ScrappingDateLogRepository;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;
import ru.vakoom.scrappingservice.scrappersystem.ScrapperMeta;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//    https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/
@Slf4j
@Service
public class HockeyBezGranizScrapper extends Scrapper {

    //TODO ДОКОСТЫЛИТЬ С ДЕТСКИМИ /*, "/detskaya-ekipirovka/"*/

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private ScrappingDateLogRepository scrappingDateLogRepository;

    @PostConstruct
    public void init() {
        scrapperMeta = ScrapperMeta.fromJson("src/main/resources/webshopconfig/hockey-bez-graniz.json");
    }

    @Override
    public List<Offer> fullCatalog() { //TODO move to scrapper
        List<String> catalogUrls = new ArrayList<>(scrapperMeta.getMenuItems());
        //ToDo подумать как убрать в аннотации
        ScrappingDateLog scrappingDateLog = new ScrappingDateLog();
        scrappingDateLog.setDateOfScrap(new Date());
        long start = System.currentTimeMillis();
        List<Offer> offers = catalogUrls.stream()
                .map(this::menuItem)
                .flatMap(List::stream)
                .peek(offerRepository::saveOrUpdate)
                .collect(Collectors.toList());
        long finish = System.currentTimeMillis();
        scrappingDateLog.setTimeOfScrapping(finish - start);
        scrappingDateLog.setShopName(scrapperMeta.getShopName());
        scrappingDateLogRepository.save(scrappingDateLog);
        return offers;
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
