package ru.vakoom.scrappingservice.shopscrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;
import ru.vakoom.scrappingservice.scrappersystem.ScrapperMeta;

import java.util.List;

@Slf4j
@Service
public class NordHockeyScrapper extends Scrapper {
    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        try {
            String pages = fullCategoryDoc.getElementsByClass("toolbar clearfix").get(0).attr("data-last-page-num");
            return Integer.valueOf(pages);
        } catch (Exception e) {
            log.warn("There is no possibility to get number of pages for some category of NORDHOCKEY");
            return 1;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scrapperMeta = ScrapperMeta.fromJson("web-shop-config/nordhockey.json");
    }

    @Override
    public Double getPrice(Element startElement, ScrapperMeta.ElementChain elementChain, ScrapperMeta meta) {
        try {
            String elementByChain = scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName() + "price");
            return parseDouble(elementByChain);
        } catch (Exception e) {
            log.warn("Price name for nordhockey shop, cant be taken for the first try");
            String elementByChain = scrapperService.getElementByChain(
                    startElement,
                    List.of(new ScrapperMeta.HtmlLocation().setName("regular-price").setHtmlObjectType("class"),
                            new ScrapperMeta.HtmlLocation().setName("price").setHtmlObjectType("class"),
                            new ScrapperMeta.HtmlLocation().setName("itemprop@price").setHtmlObjectType("attrwithvalue")),
                    meta.getShopName() + "price");
            return parseDouble(elementByChain);
        }

    }
}
