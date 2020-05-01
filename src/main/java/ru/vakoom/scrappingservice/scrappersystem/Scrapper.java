package ru.vakoom.scrappingservice.scrappersystem;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.model.ScrappingDateLog;
import ru.vakoom.scrappingservice.repository.OfferRepository;
import ru.vakoom.scrappingservice.repository.ScrappingDateLogRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public abstract class Scrapper {

    @Autowired
    protected ScrapperService scrapperService;
    protected ScrapperMeta scrapperMeta;

    @Autowired
    protected OfferRepository offerRepository;
    @Autowired
    protected ScrappingDateLogRepository scrappingDateLogRepository;

    public abstract void init();

    public List<Offer> fullCatalog() {
        List<String> menuItemUrls = new ArrayList<>(scrapperMeta.getMenuItems());
        //ToDo подумать как убрать в аннотации
        ScrappingDateLog scrappingDateLog = new ScrappingDateLog();
        scrappingDateLog.setDateOfScrap(new Date());
        long start = System.currentTimeMillis();
        List<Offer> offers = menuItemUrls.stream()
                .map(this::category)
                .flatMap(List::stream)
                .peek(offerRepository::saveOrUpdate)
                .collect(Collectors.toList());
        long finish = System.currentTimeMillis();
        scrappingDateLog.setTimeOfScrapping(finish - start);
        scrappingDateLog.setShopName(scrapperMeta.getShopName());
        scrappingDateLogRepository.save(scrappingDateLog);
        return offers;
    }

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
        String categoryName = substringForLog.substring(substringForLog.lastIndexOf("/") + 1);
        log.info("Category {} has {} pages", categoryName, pages - 1);

        return IntStream.range(1, pages)
                .mapToObj(i -> categoryUrl + scrapperMeta.getPaginatorParam() + i)
                .map(url -> productsPage(url,categoryName))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public abstract Integer defineCountOfPages(Document fullCategoryDoc);

    public List<Offer> productsPage(String pageUrl, String categoryName) {
        Document doc;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Elements products = scrapperService.getElementsByClass(doc, scrapperMeta.getRootElement().getName());
        return products.stream().map(catalogItem -> createOfferFromMeta(catalogItem, scrapperMeta, categoryName))
                .collect(Collectors.toList());
    }

    public Offer createOfferFromMeta(Element startElement, ScrapperMeta meta, String categoryName) {
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
                case "category":
                    offer.category(categoryName);
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
