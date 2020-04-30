package ru.vakoom.scrappingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.List;

@Data
@Slf4j
public class ScrapperMeta {

    private String basePath;
    private String paginatorParam;

    /**
     * Корневой элемент представляющий собой сужение html документа
     * В данном html локе находится вся интересующая нас информация по продуктам
     */
    private HtmlLocation rootElement;

    /**
     * Список html элементов при помощи уоторых собирается инфомрация о продукте
     */
    private List<ElementChain> elementChainList;

    /**
     * Html элемент необходимый для сбора инфомрации о продукте
     */
    @Data
    static class ElementChain {

        /**
         * Название свойства продукта
         * Поле в объекте продукт, значение для которого мы ищем в html
         */
        private String productField;

        /**
         * Цепочка элементов html с помощью которой мы добираемся до значения конкретного свойства продукта
         */
        private List<HtmlLocation> htmlLocationChain;
    }

    /**
     * Html элемент с помощью которого мы добираемся до значения конкретного свойства продукта
     */
    @Data
    static class HtmlLocation {

        /**
         * Навзание элемента
         */
        private String name;

        /**
         * Тип Html объекта, через который мы получаем элемент (class, tag, attr)
         */
        private String htmlObjectType;
    }

    public static ScrapperMeta fromJson(String path) {
        try {
            return new ObjectMapper().readValue(Paths.get(path).toFile(), ScrapperMeta.class);
        } catch (IOException e) {
            log.error("Converting of json to scrapper meta failed. Failed file path: {}", path);
            log.error(e.toString());
            throw new UncheckedIOException(e);
        }
    }

}
