package ru.vakoom.scrappingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.model.Product;
import ru.vakoom.scrappingservice.repository.HockeyRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//    https://hockeybezgranic.ru/catalog/konki/konki-khokkeynye/
@Slf4j
@Service
public class HockeyBezGranizScrappingService extends Scrapper {

    @Autowired
    private HockeyRepository hockeyRepository;

    @Override
    public List<Product> fullCatalog() {
        List<String> catalogUrls = List.of("/konki", "/zashchita-igroka", "/klyushki", "/odezhda", "/vratar", "/sumki", "/aksessuary", "/trenazhery", "/raznoe");
        return catalogUrls.stream()
                .map(this::menuItem)
                .flatMap(List::stream)
                .peek(hockeyRepository::saveOrUpdate)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> menuItem(String menuItemUrl) {
        String menuItemFullPath = scrapperMeta.getBasePath() + scrapperMeta.getCatalogPath() + menuItemUrl;
        Document fullMenuItemDOc = null;
        try {
            fullMenuItemDOc = Jsoup.connect(menuItemFullPath).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return scrapperService.getElementValueByClassAndAttribute(fullMenuItemDOc, "category-more")
                .stream()
                .map(catalogWithMenuItemWithCategoryPath -> category(scrapperMeta.getBasePath(), catalogWithMenuItemWithCategoryPath))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> category(String basePath, String catalogWithMenuItemWithCategoryPath) {
        String categoryUrl = basePath + catalogWithMenuItemWithCategoryPath;

        Document fullCategoryDoc;
        try {
            fullCategoryDoc = Jsoup.connect(categoryUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Integer pages = defineCountOfPages(fullCategoryDoc);

        log.info(
                "Category{} has {} pages",
                catalogWithMenuItemWithCategoryPath.substring(catalogWithMenuItemWithCategoryPath.lastIndexOf('/') + 1),
                pages - 1
        );
        return IntStream.range(1, pages)
                .mapToObj(i -> categoryUrl + scrapperMeta.getPaginatorParam() + i)
                .map(this::productsPage)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
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
}
