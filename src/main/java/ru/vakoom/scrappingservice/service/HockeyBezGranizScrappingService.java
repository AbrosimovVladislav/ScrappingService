package ru.vakoom.scrappingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Product;
import ru.vakoom.scrappingservice.repository.HockeyRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class HockeyBezGranizScrappingService {
//    https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/

    @Value("https://hockeybezgranic.ru")
    private String basePath;

    @Value("/catalog")
    private String catalogPath;

    @Value("?PAGEN_1=")
    private String paginationParam;

    private final ScrapperService scrapperService;
    private final HockeyRepository hockeyRepository;

    //Возможно можно упростить за счет вытаскивания сразу конечного класса а не всей цепочки классов
    private ScrapperMeta2 scrapperMeta2 = new ScrapperMeta2(
            Pair.of("catalog-item", "class"),
            List.of(
                    Pair.of("name", new LinkedHashMap<>() {{
                        put("ci-link", HtmlChainParam.of("class"));
                        put("ci-title", HtmlChainParam.of("lastclass"));
                    }}),
                    Pair.of("brand", new LinkedHashMap<>() {{
                        put("ci-link", HtmlChainParam.of("class"));
                        put("ci-brand", HtmlChainParam.of("lastclass"));
                    }}),
                    Pair.of("price", new LinkedHashMap<>() {{
                        put("ci-prices", HtmlChainParam.of("class"));
                        put("price", HtmlChainParam.of("lastclass"));
                    }}),
                    Pair.of("link", new LinkedHashMap<>() {{
                        put("ci-link", HtmlChainParam.of("class"));
                        put("href", HtmlChainParam.of("attr"));
                    }}),
                    Pair.of("imgLink", new LinkedHashMap<>() {{
                        put("ci-link", HtmlChainParam.of("class"));
                        put("ci-thumb", HtmlChainParam.of("class"));
                        put("img", HtmlChainParam.of("tag"));
                        put("src", HtmlChainParam.of("attr"));
                    }})
            ));

    public List<Product> catalog() {
/*
        Вытаскиваем из всего каталога сайта, например https://hockeybezgranic.ru/catalog
        Используем вытаскивание из каждого первоуровнего пункта меню
         */
        List<String> catalogUrls = List.of("/konki", "/zashchita-igroka", "/klyushki", "/odezhda", "/vratar", "/sumki", "/aksessuary", "/trenazhery", "/raznoe");

        return catalogUrls.stream()
                .map(this::menuItem)
                .flatMap(List::stream)
                .peek(hockeyRepository::save)
                .collect(Collectors.toList());
    }

    public List<Product> menuItem(String menuItemUrl) {
/*
        Вытаскиваем из первоуровнего пункта меню, например https://hockeybezgranic.ru/catalog/konki/
        Используем вытаскивание из каждой категории
         */

        String menuItemFullPath = basePath + catalogPath + menuItemUrl;
        Document fullMenuItemDOc = null;
        try {
            fullMenuItemDOc = Jsoup.connect(menuItemFullPath).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return scrapperService.getElementValueByClassAndAttribute(fullMenuItemDOc, "category-more")
                .stream()
                .map(catalogWithMenuItemWithCategoryPath -> category(basePath, catalogWithMenuItemWithCategoryPath))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Product> category(String basePath, String catalogWithMenuItemWithCategoryPath) {
        /*
        Вытаскиваем из категории, например https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/
        https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/?PAGEN_1=2
        Используем постраничное вытаскивание
         */

        String categoryUrl = basePath + catalogWithMenuItemWithCategoryPath;

        Document fullCategoryDoc;
        try {
            fullCategoryDoc = Jsoup.connect(categoryUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Integer pages = defineCountOfPages(fullCategoryDoc);
        log.info(catalogWithMenuItemWithCategoryPath + ": " + (pages - 1));
        return IntStream.range(1, pages)
                .mapToObj(i -> categoryUrl + paginationParam + i)
                .map(this::productsPage)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Integer defineCountOfPages(Document fullCategoryDoc) {
        //ToDo Implement here
        int lastPageNumber = 1;
        Elements pageNav = fullCategoryDoc.getElementsByClass("page-nav");
        if (pageNav.isEmpty()) return ++lastPageNumber;
        for (Element hrefE : pageNav.get(0).getElementsByAttribute("href")) {
            int currentPageNumber = Integer.parseInt(hrefE.text());
            if (lastPageNumber < currentPageNumber) {
                lastPageNumber = currentPageNumber;
            }
        }
        return ++lastPageNumber;
    }

    public List<Product> productsPage(String pageUrl) {
        Document doc = null;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements products = scrapperService.getElementsByClass(doc, scrapperMeta2.getRootElement().getFirst());
        //ToDo подумать как исправить рефреш по правильному
        refreshScrapperMeta2();
        List<Product> p = products.stream().map(catalogItem -> {
            refreshScrapperMeta2();
            return createProductFromMeta(catalogItem, scrapperMeta2);
        }).collect(Collectors.toList());

        return p;
    }

    public Product createProductFromMeta(Element startElement, ScrapperMeta2 meta2) {
        Product product = new Product();
        for (Pair<String, LinkedHashMap<String, HtmlChainParam>> meta : meta2.elementChain) {
            if (meta.getFirst().equals("name")) {
                product.name(scrapperService.getElementByChain(startElement, meta.getSecond()));
            } else if (meta.getFirst().equals("brand")) {
                product.brand(scrapperService.getElementByChain(startElement, meta.getSecond()));
            } else if (meta.getFirst().equals("price")) {
                product.price(scrapperService.getElementByChain(startElement, meta.getSecond()));
            } else if (meta.getFirst().equals("link")) {
                product.link(scrapperService.getElementByChain(startElement, meta.getSecond()));
            } else if (meta.getFirst().equals("imgLink")) {
                product.imgLink(scrapperService.getElementByChain(startElement, meta.getSecond()));
            }
        }
        log.info(product.toString());
        return product;
    }

    private void refreshScrapperMeta2() {
        //Используется для возвращения мапы в исходное состояние.
        // Так как происходят удаления элементов из списка в процессе составления цепи
        scrapperMeta2 = new ScrapperMeta2(
                Pair.of("catalog-item", "class"),
                List.of(
                        Pair.of("name", new LinkedHashMap<>() {{
                            put("ci-link", HtmlChainParam.of("class"));
                            put("ci-title", HtmlChainParam.of("lastclass"));
                        }}),
                        Pair.of("brand", new LinkedHashMap<>() {{
                            put("ci-link", HtmlChainParam.of("class"));
                            put("ci-brand", HtmlChainParam.of("lastclass"));
                        }}),
                        Pair.of("price", new LinkedHashMap<>() {{
                            put("ci-prices", HtmlChainParam.of("class"));
                            put("price", HtmlChainParam.of("lastclass"));
                        }}),
                        Pair.of("link", new LinkedHashMap<>() {{
                            put("ci-link", HtmlChainParam.of("class"));
                            put("href", HtmlChainParam.of("attr"));
                        }}),
                        Pair.of("imgLink", new LinkedHashMap<>() {{
                            put("ci-link", HtmlChainParam.of("class"));
                            put("ci-thumb", HtmlChainParam.of("class"));
                            put("img", HtmlChainParam.of("tag"));
                            put("src", HtmlChainParam.of("attr"));
                        }})
                ));
    }

}
