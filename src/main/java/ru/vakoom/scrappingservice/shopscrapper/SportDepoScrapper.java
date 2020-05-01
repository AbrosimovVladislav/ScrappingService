package ru.vakoom.scrappingservice.shopscrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;
import ru.vakoom.scrappingservice.scrappersystem.ScrapperMeta;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class SportDepoScrapper extends Scrapper {

    @Override
    @PostConstruct
    public void init() {
        scrapperMeta = ScrapperMeta.fromJson("src/main/resources/web-shop-config/hockeybezgranic.json");
    }

    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        return null;
    }
}
