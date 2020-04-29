package ru.vakoom.scrappingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;

@Slf4j
public enum ScrapperMethod {

    CLASS_CHAIN {
        @Override
        public String getValue(Element startElement, List<String> classChain, Map<String, String> additionalParams) {
            for (String className : classChain) {
                startElement = startElement.getElementsByClass(className).get(0);
            }
            return startElement.text();
        }
    },
    CLASS_CHAIN_AND_ATTR {
        @Override
        public String getValue(Element startElement, List<String> classChain, Map<String, String> additionalParams) {
            for (String className : classChain) {
                startElement = startElement.getElementsByClass(className).get(0);
            }
            if(additionalParams.containsKey("attr")){
                return startElement.attr(additionalParams.get("attr"));
            } else {
                log.error("Not correct value in {}",startElement + " " + classChain);
                return "";
            }

        }
    },
    CLASS_CHAIN_WITH_TAG_AND_ATTR {
        @Override
        public String getValue(Element startElement, List<String> classChain, Map<String, String> additionalParams) {
            for (String className : classChain) {
                startElement = startElement.getElementsByClass(className).get(0);
            }
            if(additionalParams.containsKey("tag") && additionalParams.containsKey("attr")) {
                return startElement.getElementsByTag(additionalParams.get("tag")).get(0).attr(additionalParams.get("attr"));
            } else {
                log.error("Not correct value in {}",startElement + " " + classChain);
                return "";
            }
        }
    };

    public abstract String getValue(Element startElement, List<String> classChain, Map<String, String> additionalParams);
}
