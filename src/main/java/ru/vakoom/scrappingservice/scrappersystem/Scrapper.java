package ru.vakoom.scrappingservice.scrappersystem;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
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
public abstract class Scrapper implements InitializingBean {

    @Autowired
    protected ScrapperService scrapperService;
    @Autowired
    protected OfferRepository offerRepository;
    @Autowired
    protected ScrappingDateLogRepository scrappingDateLogRepository;

    protected ScrapperMeta scrapperMeta;

    public List<Offer> fullCatalog() {
        //ToDo если из скрапа придет пустой список, он не должен дойти до агрегатора. Лучше сохранить старые данные чем пустоту
        List<ScrapperMeta.MenuItem> menuItemUrls = new ArrayList<>(scrapperMeta.getMenuItems());
        //ToDo подумать как убрать в аннотации
        ScrappingDateLog scrappingDateLog = new ScrappingDateLog();
        scrappingDateLog.setDateOfScrap(new Date());
        long start = System.currentTimeMillis();
        List<Offer> offers = menuItemUrls.stream()
                .map(this::category)
                .peek(offerRepository::saveAll)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        long finish = System.currentTimeMillis();
        scrappingDateLog.setTimeOfScrapping(finish - start);
        scrappingDateLog.setShopName(scrapperMeta.getShopName());
        scrappingDateLog.setCountOfRecords(offers.size());
        scrappingDateLogRepository.save(scrappingDateLog);
        return offers;
    }

    private List<Offer> category(ScrapperMeta.MenuItem menuItem) {
        Document categoryDoc;
        try {
            categoryDoc = Jsoup.connect(menuItem.getUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Integer pages = defineCountOfPages(categoryDoc);

        log.info("Category {} has {} pages", menuItem.getCategoryName(), pages);

        //Range takes form first page to last not inclusive. That is why using +1s
        return IntStream.range(1, pages + 1)
                .mapToObj(i -> menuItem.getUrl() + scrapperMeta.getPaginatorParam() + i)
                .map(url -> productsPage(url, menuItem.getCategoryName()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public abstract Integer defineCountOfPages(Document fullCategoryDoc);

    private List<Offer> productsPage(String pageUrl, String categoryName) {
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

    private Offer createOfferFromMeta(Element startElement, ScrapperMeta meta, String categoryName) {
        Offer offer = new Offer();
        for (ScrapperMeta.ElementChain elementChain : meta.getElementChainList()) {
            switch (elementChain.getProductField()) {
                case "name":
                    offer.setName(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "brand":
                    offer.setBrand(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "price":
                    offer.setPrice(parseDouble(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain())));
                    break;
                case "inStore":
                    offer.setInStore(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain())
                            .equalsIgnoreCase("купить") ||
                            scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain())
                                    .contains("InStock"));
                    break;
                case "category":
                    offer.setCategory(categoryName);
                    break;
                case "shopName":
                    offer.setShopName(scrapperMeta.getShopName());
                    break;
                case "link":
                    offer.setLink(scrapperMeta.getBasePath() + scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
            }
        }
        log.info(offer.toString());
        return offer;
    }

    private Double parseDouble(String price) {
        String onlyDoubleRegex = "[^0-9]";
        price = price.replaceAll(onlyDoubleRegex, "");
        price = price.replace(",", ".");
        return Double.parseDouble(price);
    }
}
