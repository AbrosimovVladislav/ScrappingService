package ru.vakoom.scrappingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

@Slf4j
public enum HtmlChainParam {

    CLASS {
        @Override
        public Object getValueByParam(Element startElement, String paramName) {
            return startElement.getElementsByClass(paramName).get(0);
        }
    },
    LASTCLASS {
        @Override
        public Object getValueByParam(Element startElement, String paramName) {
            return startElement.getElementsByClass(paramName).get(0).text();
        }
    },
    TAG {
        @Override
        public Object getValueByParam(Element startElement, String paramName) {
            return startElement.getElementsByTag(paramName).get(0);
        }
    },
    LASTTAG {
        @Override
        public Object getValueByParam(Element startElement, String paramName) {
            return startElement.getElementsByTag(paramName).get(0).text();
        }
    },
    ATTR {
        @Override
        public Object getValueByParam(Element startElement, String paramName) {
            return startElement.attr(paramName);
        }
    };

    public static HtmlChainParam of(String value) {
        if (value.equalsIgnoreCase(CLASS.toString())) {
            return CLASS;
        } else if (value.equalsIgnoreCase(LASTCLASS.toString())) {
            return LASTCLASS;
        } else if (value.equalsIgnoreCase(TAG.toString())) {
            return TAG;
        } else if (value.equalsIgnoreCase(LASTTAG.toString())) {
            return LASTTAG;
        } else if (value.equalsIgnoreCase(ATTR.toString())) {
            return ATTR;
        } else {
            log.error("HtmlChainParam with that name pipiska");
            return CLASS;
        }
    }

    public abstract Object getValueByParam(Element startElement, String paramName);

}
