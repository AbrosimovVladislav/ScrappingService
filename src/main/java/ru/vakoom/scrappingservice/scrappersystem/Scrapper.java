package ru.vakoom.scrappingservice.scrappersystem;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vakoom.scrappingservice.model.Brand;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.model.ScrappingDateLog;
import ru.vakoom.scrappingservice.model.Type;
import ru.vakoom.scrappingservice.repository.BrandRepository;
import ru.vakoom.scrappingservice.repository.OfferRepository;
import ru.vakoom.scrappingservice.repository.ScrappingDateLogRepository;
import ru.vakoom.scrappingservice.repository.TypeRepository;

import java.io.IOException;
import java.util.*;
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
    @Autowired
    protected TypeRepository typeRepository;
    @Autowired
    protected BrandRepository brandRepository;

    protected Offer offer;
    protected ScrapperMeta scrapperMeta;

    public List<Offer> fullCatalog() {
        List<ScrapperMeta.MenuItem> menuItemUrls = new ArrayList<>(scrapperMeta.getMenuItems());
        //ToDo подумать как убрать в аннотации
        ScrappingDateLog scrappingDateLog = new ScrappingDateLog();
        scrappingDateLog.setDateOfScrap(new Date());
        long start = System.currentTimeMillis();
        List<Offer> offers = menuItemUrls.parallelStream()
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
        Optional<Document> categoryDoc = getDocByUrl(menuItem.getUrl());
        if (categoryDoc.isEmpty()) {
            return Collections.emptyList();
        }

        Integer pages = defineCountOfPages(categoryDoc.get());

        Type type;
        Optional<Type> optionalType = typeRepository.findById(menuItem.getTypeId());
        if (optionalType.isPresent()) {
            type = optionalType.get();
        } else {
            log.error("There is no such type id: {}", menuItem.getTypeId());
            return Collections.emptyList();
        }

        if (pages == 0) {
            log.info("Category {} has {} pages. No offers here", type.getShowName(), pages);
            return Collections.emptyList();
        }

        log.info("Category {} has {} pages", type.getShowName(), pages);

        //Range takes form first page to last not inclusive. That is why using +1s
        List<Offer> offersToSend = IntStream.range(1, pages + 1)
                .mapToObj(i -> addPaginationToUrl(menuItem.getUrl(), scrapperMeta.getPaginatorParam(), i))
                .map(url -> productsPage(url, type))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if (offersToSend.isEmpty()) log.error("This category {} has no offers inside", categoryDoc.get());
        return offersToSend;
    }

    private String addPaginationToUrl(String url, String paginator, Integer pageNumber) {
        if (scrapperMeta.getShopName().equalsIgnoreCase("KLUSHKI")) {
            return url;
        }
        return url.contains("?") ? url + "&" + paginator + pageNumber : url + "?" + paginator + pageNumber;
    }

    public abstract Integer defineCountOfPages(Document fullCategoryDoc);

    private List<Offer> productsPage(String pageUrl, Type type) {
        Optional<Document> productPageDoc = getDocByUrl(pageUrl);
        if (productPageDoc.isEmpty()) {
            return Collections.emptyList();
        }

        Elements products = scrapperService.getElementsByClass(productPageDoc.get(), scrapperMeta.getRootElement().getName());
        return products.stream().map(catalogItem -> createOfferFromMeta(catalogItem, scrapperMeta, type))
                .peek(o -> offer = o)
                .map(o -> postHandle())
                .collect(Collectors.toList());
    }

    private Offer createOfferFromMeta(Element startElement, ScrapperMeta meta, Type type) {
        Offer offer = new Offer();
        for (ScrapperMeta.ElementChain elementChain : meta.getElementChainList()) {
            switch (elementChain.getProductField()) {
                case "name":
                    offer.setName(
                            scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                    );
                    break;
                case "brand":
                    offer.setBrand(getBrandName(startElement, elementChain.getHtmlLocationChain(), offer.getName(), scrapperMeta.getShopName()));
                    break;
                case "price":
                    offer.setPrice(
                            getPrice(startElement, elementChain, meta)
//                            parseDouble(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName() + "price"))
                    );
                    break;
                case "inStore":
                    if (offer.getPrice() == 0D || offer.getPrice() == 0) {
                        offer.setInStore(false);
                    } else {
                        offer.setInStore(
                                scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                                        .equalsIgnoreCase("купить") ||
                                        scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                                                .equalsIgnoreCase("Есть в наличии") ||
                                        scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                                                .equalsIgnoreCase("В корзину") ||
                                        scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                                                .contains("InStock") ||
                                        scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                                                .equalsIgnoreCase("Много") ||
                                        scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                                                .equalsIgnoreCase("Достаточно")
                        );
                    }
                    break;
                case "link":
                    if (scrapperMeta.getShopName().equalsIgnoreCase("HOCK5")
                            || scrapperMeta.getShopName().equalsIgnoreCase("KLUSHKI")
                            || scrapperMeta.getShopName().equalsIgnoreCase("NORDHOCKEY")) {
                        offer.setLink(
                                scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                        );
                    } else {
                        offer.setLink(
                                scrapperMeta.getBasePath() + scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName())
                        );
                    }
                    break;
            }
        }
        offer.setShopName(scrapperMeta.getShopName());
        offer.setType(type);
        offer.setAge(getAgeFromOfferName(offer.getName()));
        log.info(offer.toString());
        return offer;
    }

    public Double getPrice(Element startElement, ScrapperMeta.ElementChain elementChain, ScrapperMeta meta){
        return parseDouble(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain(), meta.getShopName() + "price"));
    }

    private String getBrandName(Element element, List<ScrapperMeta.HtmlLocation> htmlLocationChain, String modelName, String shopName) {
        String brandName = null;
        try {
            brandName = scrapperService.getElementByChain(element, htmlLocationChain, shopName);

        } catch (Exception e) {
            log.warn("Brand name for {}, cant be taken for the first try", modelName);
        }
        if (brandName == null || brandName.isBlank()) {
            List<Brand> brands = brandRepository.findAll();
            brandName = brands.stream()
                    .filter(b -> modelName.toUpperCase().contains(b.getShortName().toUpperCase()))
                    .findAny()
                    .map(Brand::getShortName)
                    .orElse("");
        }
        return brandName;
    }

    private String getAgeFromOfferName(String offerName) {
        String offerNameInLC = offerName.toLowerCase();

        if (offerNameInLC.contains("sr") || offerNameInLC.contains("senior")) {
            return "SR";
        }
        if (offerNameInLC.contains("jr") || offerNameInLC.contains("junior")) {
            return "JR";
        }
        if (offerNameInLC.contains("yth") || offerNameInLC.contains("youth")) {
            return "YTH";
        }
        if (offerNameInLC.contains("int") || offerNameInLC.contains("intermediate") || offerNameInLC.contains("intermed")) {
            return "INT";
        }
        return "";
    }

    public Double parseDouble(String price) {
        String onlyDoubleRegex = "[^0-9]";
        price = price.replaceAll(onlyDoubleRegex, "");
        price = price.replace(",", ".");
        return Double.parseDouble(price);
    }

    protected Optional<Document> getDocByUrl(String url) {
        try {
            if (url.contains("forma")) {
                return Optional.of(Jsoup.connect(url).userAgent("Mozilla").get());
            }
            return Optional.of(Jsoup.connect(url).get());
        } catch (IOException e) {
            log.error("{} url: {}", e.getMessage(), url);
            return Optional.empty();
        }
    }

    protected Offer postHandle() {
        return offer;
    }
}
