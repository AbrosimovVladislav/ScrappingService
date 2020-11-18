package ru.vakoom.scrappingservice.service.shopscrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.service.scrappersystem.Scrapper;
import ru.vakoom.scrappingservice.service.scrappersystem.ScrapperMeta;

@Slf4j
@Service
public class KlushkiScrapper extends Scrapper {

    public void afterPropertiesSet() {
        scrapperMeta = ScrapperMeta.fromJson("web-shop-config/klushki.json");
    }

    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        if(scrapperMeta.getShopName().equalsIgnoreCase("KLUSHKI")){
            return 1;
        }

        if(!fullCategoryDoc.getElementsByClass("wrap_text_empty").isEmpty()){
            return 0;
        }
        Elements paginationElements = fullCategoryDoc.getElementsByClass("nums");
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
