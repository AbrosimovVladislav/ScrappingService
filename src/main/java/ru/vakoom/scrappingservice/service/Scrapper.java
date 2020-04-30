package ru.vakoom.scrappingservice.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vakoom.scrappingservice.model.Product;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class Scrapper {

    @Autowired
    protected ScrapperService scrapperService;
    protected ScrapperMeta scrapperMeta;

    @PostConstruct
    public void init() {
        scrapperMeta = ScrapperMeta.fromJson("src/main/resources/hockey-bez-graniz.json");
    }

    public abstract List<Product> fullCatalog();

    public abstract List<Product> menuItem(String menuItemUrl);

    public abstract List<Product> category(String basePath, String catalogWithMenuItemWithCategoryPath);

    public abstract Integer defineCountOfPages(Document fullCategoryDoc);

    public List<Product> productsPage(String pageUrl) {
        Document doc;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Elements products = scrapperService.getElementsByClass(doc, scrapperMeta.getRootElement().getName());
        return products.stream().map(catalogItem -> createProductFromMeta(catalogItem, scrapperMeta))
                .collect(Collectors.toList());
    }

    public Product createProductFromMeta(Element startElement, ScrapperMeta meta) {
        Product product = new Product();
        for (ScrapperMeta.ElementChain elementChain : meta.getElementChainList()) {
            switch (elementChain.getProductField()) {
                case "name":
                    product.name(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "brand":
                    product.brand(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "price":
                    product.price(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "link":
                    product.link(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
                case "imgLink":
                    product.imgLink(scrapperService.getElementByChain(startElement, elementChain.getHtmlLocationChain()));
                    break;
            }
        }
        log.info(product.toString());
        return product;
    }

}
