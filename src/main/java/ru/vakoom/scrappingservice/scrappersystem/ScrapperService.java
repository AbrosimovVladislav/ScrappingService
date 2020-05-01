package ru.vakoom.scrappingservice.scrappersystem;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapperService {

    public String getElementByChain(Element startElement, List<ScrapperMeta.HtmlLocation> paramChain) {
        var paramChainWithoutLastElement = new ArrayList<>(paramChain);
        ScrapperMeta.HtmlLocation lastElement = paramChainWithoutLastElement.get(paramChainWithoutLastElement.size() - 1);
        paramChainWithoutLastElement.remove(lastElement);

        for (ScrapperMeta.HtmlLocation htmlLocation : paramChainWithoutLastElement) {
            String paramName = htmlLocation.getName();
            HtmlObjectType htmlObjectType = HtmlObjectType.of(htmlLocation.getHtmlObjectType());
            startElement = (Element) htmlObjectType.getValueByParam(startElement, paramName);
        }

        HtmlObjectType lastElementHtmlObjectType = HtmlObjectType.of(lastElement.getHtmlObjectType());

        return (String) lastElementHtmlObjectType.getValueByParam(startElement, lastElement.getName());
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
