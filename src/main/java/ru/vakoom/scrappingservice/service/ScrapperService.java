package ru.vakoom.scrappingservice.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Elements getElementsByClass(Document doc, String className) {
        return doc.getElementsByClass(className);
    }

    //ToDo убрать, переделать через общий алгортим (LinkedHashMap)
    public List<String> getElementValueByClassAndAttribute(Document doc, String className) {
        return doc.getElementsByClass(className).stream()
                .map(element -> element.attr("href"))
                .collect(Collectors.toList());
    }


}
