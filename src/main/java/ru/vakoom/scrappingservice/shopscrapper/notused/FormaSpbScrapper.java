package ru.vakoom.scrappingservice.shopscrapper.notused;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;
import ru.vakoom.scrappingservice.scrappersystem.ScrapperMeta;

@Slf4j
//@Service
public class FormaSpbScrapper extends Scrapper {

    public void afterPropertiesSet() {
        scrapperMeta = ScrapperMeta.fromJson("web-shop-config/formaspb.json");
    }

    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        if(!fullCategoryDoc.getElementsByClass("wrap_text_empty").isEmpty()){
            return 0;
        }
        Elements paginationElements = fullCategoryDoc.getElementsByClass("module-pagination");
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
