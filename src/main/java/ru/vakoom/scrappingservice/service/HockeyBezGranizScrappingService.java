package ru.vakoom.scrappingservice.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.vakoom.scrappingservice.service.ScrapperMethod.*;

@Service
public class HockeyBezGranizScrappingService {
//    https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/

    @Value("https://hockeybezgranic.ru")
    private String basePath;

    @Value("/catalog")
    private String catalogPath;

    @Value("/konki/konki-khokkeynye/")
    private String currentCategory;

    private Map<String, List<ScrapperMeta>> productScrapperChain = Map.of(
            "catalog-item", List.of(
                    new ScrapperMeta().productParamName("name").scrapperMethod(CLASS_CHAIN).classChain(List.of("ci-link", "ci-title")).additionalParams(null),
                    new ScrapperMeta().productParamName("brand").scrapperMethod(CLASS_CHAIN).classChain(List.of("ci-link", "ci-brand")).additionalParams(null),
                    new ScrapperMeta().productParamName("price").scrapperMethod(CLASS_CHAIN).classChain(List.of("ci-prices", "price")).additionalParams(null),
                    new ScrapperMeta().productParamName("link").scrapperMethod(CLASS_CHAIN_AND_ATTR).classChain(List.of("ci-link")).additionalParams(Map.of("attr", "href")),
                    new ScrapperMeta().productParamName("imgLink").scrapperMethod(CLASS_CHAIN_WITH_TAG_AND_ATTR).classChain(List.of("ci-link", "ci-thumb")).additionalParams(Map.of("tag", "img", "attr", "src"))
            )
    );

    public List<Product> catalog(){
/*
        Вытаскиваем из всего каталога сайта, например https://hockeybezgranic.ru/catalog
        Используем вытаскивание из каждого первоуровнего пункта меню
         */
        return null;
    }

    public List<Product> menuItem(){
/*
        Вытаскиваем из первоуровнего пункта меню, например https://hockeybezgranic.ru/catalog/konki/
        Используем вытаскивание из каждой категории
         */
        return null;
    }

    public List<Product> category(){
        /*
        Вытаскиваем из категории, например https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/
        Используем постраничное вытаскивание
         */
        return null;
    }

    public List<Product> productsPage() {
        Document doc = null;
        try {
            doc = Jsoup.connect(basePath + catalogPath + currentCategory).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String startItemClassName = productScrapperChain.keySet().iterator().next();
        Elements products = doc.getElementsByClass(startItemClassName);
        return products.stream().map(catalogItem -> createProductFromMeta(catalogItem, productScrapperChain.get(startItemClassName)))
                .collect(Collectors.toList());
    }

    private Product createProductFromMeta(Element startElement, List<ScrapperMeta> metas) {
        Product product = new Product();
        for (ScrapperMeta meta : metas) {
            if (meta.productParamName().equals("name")) {
                product.name(getValueByMethod(startElement, meta));
            } else if (meta.productParamName().equals("brand")) {
                product.brand(getValueByMethod(startElement, meta));
            } else if (meta.productParamName().equals("price")) {
                product.price(getValueByMethod(startElement, meta));
            } else if (meta.productParamName().equals("link")) {
                product.link(getValueByMethod(startElement, meta));
            } else if (meta.productParamName().equals("imgLink")) {
                product.imgLink(getValueByMethod(startElement, meta));
            }
        }
        return product;
    }

    private String getValueByMethod(Element startElement, ScrapperMeta meta) {
        ScrapperMethod method = meta.scrapperMethod();
        return method.getValue(startElement, meta.classChain(), meta.additionalParams());
    }

/*    public List<Product> simplePageScrapper() {
        Document doc = null;
        try {
            doc = Jsoup.connect(basePath + catalogPath + currentCategory).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements products = doc.getElementsByClass("catalog-item");
        return products.stream().map(catalogItem ->
                new Product()
                        .name(getValueByClass(catalogItem, List.of("ci-link", "ci-title")))
                        .brand(getValueByClass(catalogItem, List.of("ci-link", "ci-brand")))
                        .price(getValueByClass(catalogItem, List.of("ci-prices", "price")))
                        .link(getValueByClassAttr(catalogItem, List.of("ci-link"), "href"))
                        .imgLink(getValueByClassTagAttr(catalogItem, List.of("ci-link", "ci-thumb"), "img", "src")))
                .collect(Collectors.toList());
    }*/

/*    private String getValueByClass(Element startElement, List<String> classChain) {
        for (String className : classChain) {
            startElement = startElement.getElementsByClass(className).get(0);
        }
        return startElement.text();
    }

    private String getValueByClassAttr(Element startElement, List<String> classChain, String attr) {
        for (String className : classChain) {
            startElement = startElement.getElementsByClass(className).get(0);
        }
        return startElement.attr(attr);
    }

    private String getValueByClassTagAttr(Element startElement, List<String> classChain, String tag, String attr) {
        for (String className : classChain) {
            startElement = startElement.getElementsByClass(className).get(0);
        }
        return startElement.getElementsByTag(tag).get(0).attr(attr);
    }*/

}
