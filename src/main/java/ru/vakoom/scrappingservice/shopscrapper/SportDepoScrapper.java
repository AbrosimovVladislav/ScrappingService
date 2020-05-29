package ru.vakoom.scrappingservice.shopscrapper;

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
@Service
public class SportDepoScrapper extends Scrapper {
    @PostConstruct
    public void afterPropertiesSet() {
        scrapperMeta = ScrapperMeta.fromJson("web-shop-config/sprotdepo.json");
    }

    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        Elements paginationElements = fullCategoryDoc.getElementsByClass("page_list");
        if (paginationElements.isEmpty()) {
            return 1;
        }
        return paginationElements.get(0).getElementsByTag("a").stream()
                .map(Element::text)
                .filter(StringUtil::isNumeric)
                .mapToInt(Integer::parseInt)
                .max().getAsInt();
    }
}
