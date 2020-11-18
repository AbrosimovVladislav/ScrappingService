package ru.vakoom.scrappingservice.service.scrappersystem;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScrapperService {

    public String getElementByChain(Element startElement, List<ScrapperMeta.HtmlLocation> paramChain, String shopName) {
        var paramChainWithoutLastElement = new ArrayList<>(paramChain);
        ScrapperMeta.HtmlLocation lastElement = paramChainWithoutLastElement.get(paramChainWithoutLastElement.size() - 1);
        paramChainWithoutLastElement.remove(lastElement);

        for (ScrapperMeta.HtmlLocation htmlLocation : paramChainWithoutLastElement) {
            String paramName = htmlLocation.getName();
            HtmlObjectType htmlObjectType = HtmlObjectType.of(htmlLocation.getHtmlObjectType());
            startElement = (Element) htmlObjectType.getValueByParam(startElement, paramName, shopName);
        }

        HtmlObjectType lastElementHtmlObjectType = HtmlObjectType.of(lastElement.getHtmlObjectType());

        return (String) lastElementHtmlObjectType.getValueByParam(startElement, lastElement.getName(), shopName);
    }

    public Elements getElementsByClass(Document doc, String className) {
        return doc.getElementsByClass(className);
    }

}
