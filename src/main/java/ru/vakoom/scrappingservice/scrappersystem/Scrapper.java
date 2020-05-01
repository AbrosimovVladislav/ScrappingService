package ru.vakoom.scrappingservice.scrappersystem;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vakoom.scrappingservice.model.Offer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public abstract class Scrapper {

    @Autowired
    protected ScrapperService scrapperService;
    protected ScrapperMeta scrapperMeta;

    @PostConstruct
    public void init() {
        scrapperMeta = ScrapperMeta.fromJson("src/main/resources/webshopconfig/hockey-bez-graniz.json");
    }

    public abstract List<Offer> fullCatalog();

    public abstract List<Offer> menuItem(String menuItemUrl);

    public List<Offer> category(String categoryUrl) {
        Document categoryDoc;
        try {
            categoryDoc = Jsoup.connect(categoryUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Integer pages = defineCountOfPages(categoryDoc);

        String substringForLog = categoryUrl.substring(0, categoryUrl.length() - 1);
        log.info("Category {} has {} pages", substringForLog.substring(substringForLog.lastIndexOf("/") + 1), pages - 1);

        return IntStream.range(1, pages)
                .mapToObj(i -> categoryUrl + scrapperMeta.getPaginatorParam() + i)
                .map(this::productsPage)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public abstract Integer defineCountOfPages(Document fullCategoryDoc);

    public List<Offer> productsPage(String pageUrl) {
        Document doc;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Elements products = scrapperService.getElementsByClass(doc, scrapperMeta.getRootElement().getName());
        return products.stream().map(catalogItem -> createOfferFromMeta(catalogItem, scrapperMeta))
                .collect(Collectors.toList());
    }

    public Offer createOfferFromMeta(Element startElement, ScrapperMeta meta) {
        Offer offer = new Offer();
        for (ScrapperMeta.ElementChain elementChain : meta.getElementChainList()) {
            switch (elementChain.getProductField()) {
                case "name":
                    offer.name(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "brand":
                    offer.brand(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "price":
                    offer.price(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "inStore":
                    offer.inStore(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain())
                            .equalsIgnoreCase("купить"));
                    break;
                case "shopName":
                    offer.shopName(scrapperMeta.getShopName());
                    break;
                case "link":
                    offer.link(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "imgLink":
                    offer.imgLink(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
            }
        }
        log.info(offer.toString());
        return offer;
    }

}
