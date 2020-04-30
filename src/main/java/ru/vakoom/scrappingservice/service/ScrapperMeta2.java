package ru.vakoom.scrappingservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapperMeta2 {

    /**
     * Root element defined by htmlParam and name of that param ex("catalog-item","class")
     */
    Pair<String, String> rootElement;

    /**
     * List of :
     * Pair -> entityParamName <-> chain of htmlParams in Map format
     */
    List<Pair<String, LinkedHashMap<String, HtmlChainParam>>> elementChain;

}
