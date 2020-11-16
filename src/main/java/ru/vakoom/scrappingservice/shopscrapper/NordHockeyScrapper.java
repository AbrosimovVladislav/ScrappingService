package ru.vakoom.scrappingservice.shopscrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.vakoom.scrappingservice.scrappersystem.Scrapper;

@Slf4j
@Service
public class NordHockeyScrapper extends Scrapper {
    @Override
    public Integer defineCountOfPages(Document fullCategoryDoc) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
