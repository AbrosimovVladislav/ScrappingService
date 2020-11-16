package ru.vakoom.scrappingservice.shopscrapper.notused;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;
import ru.vakoom.scrappingservice.scrappersystem.ScrapperMeta;

import javax.annotation.PostConstruct;

@Slf4j
//@Service
public class Hock51Scrapper extends Scrapper {
    @PostConstruct
    public void afterPropertiesSet() {
        scrapperMeta = ScrapperMeta.fromJson("web-shop-config/hock5-1.json");
    }

    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        Elements paginationElements = fullCategoryDoc.getElementsByClass("col-sm-6 text-right");
        String paginatorText = paginationElements.get(0).text();
        paginatorText = paginatorText.substring(paginatorText.indexOf("("));
        paginatorText = paginatorText.replaceAll( "[^\\d]", "" );
        return Integer.parseInt(paginatorText);
    }
}
