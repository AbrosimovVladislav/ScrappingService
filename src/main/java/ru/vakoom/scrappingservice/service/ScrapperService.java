package ru.vakoom.scrappingservice.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ScrapperService {

    public String getElementByChain(Element startElement, LinkedHashMap<String, HtmlChainParam> paramChain) {
        /*Получение последнего элемента и удаление его из мапы*/
        Map.Entry<String, HtmlChainParam> lastElement = (Map.Entry) paramChain.entrySet().toArray()[paramChain.keySet().size() - 1];
        paramChain.remove(lastElement.getKey());

        for (var entry : paramChain.entrySet()) {
            String paramName = entry.getKey();
            HtmlChainParam htmlChainParam = entry.getValue();
            startElement = (Element) htmlChainParam.getValueByParam(startElement, paramName);
        }

        return (String) lastElement.getValue().getValueByParam(startElement, lastElement.getKey());
    }

    public Elements getElementsByClass(Document doc, String className){
        return doc.getElementsByClass(className);
    }


}
