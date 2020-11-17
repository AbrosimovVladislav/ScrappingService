package ru.vakoom.scrappingservice.scrappersystem;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public enum HtmlObjectType {

    CLASS {
        @Override
        public Object getValueByParam(Element startElement, String paramName, String shopName) {
            if(shopName.equalsIgnoreCase("ALLHOCKEYprice")){
                Elements elementsByClass = startElement.getElementsByClass(paramName);
                if(elementsByClass.isEmpty()){
                    Elements elementsByClass2 = startElement.getElementsByClass("price_matrix_wrapper");
                    return elementsByClass2.get(0);
                } else {
                    return startElement.getElementsByClass(paramName).get(1);
                }
            }
            return startElement.getElementsByClass(paramName).get(0);
        }
    },
    LASTCLASS {
        @Override
        public Object getValueByParam(Element startElement, String paramName, String shopName) {
            return startElement.getElementsByClass(paramName).get(0).text();
        }
    },
    TAG {
        @Override
        public Object getValueByParam(Element startElement, String paramName, String shopName) {
            return startElement.getElementsByTag(paramName).get(0);
        }
    },
    LASTTAG {
        @Override
        public Object getValueByParam(Element startElement, String paramName, String shopName) {
            return startElement.getElementsByTag(paramName).get(0).text();
        }
    },
    ATTR {
        @Override
        public Object getValueByParam(Element startElement, String paramName, String shopName) {
            return startElement.attr(paramName);
        }
    },
    ATTRWITHVALUE {
        @Override
        public Object getValueByParam(Element startElement, String paramName, String shopName) {
            return startElement.getElementsByAttributeValue(
                    paramName.substring(0, paramName.indexOf("@")),
                    paramName.substring(paramName.indexOf("@") + 1)
            ).text();
        }
    },
    FINDELEMENTBYATTRWITHVALUEANDGETATTR {
        @Override
        public Object getValueByParam(Element startElement, String paramName, String shopName) {
            String[] params = paramName.split("@");
            return startElement.getElementsByAttributeValue(
                    params[0],
                    params[1])
                    .get(0).attr(params[2]);
        }
    };

    public static HtmlObjectType of(String value) {
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
        } else if (value.equalsIgnoreCase(ATTRWITHVALUE.toString())) {
            return ATTRWITHVALUE;
        } else if (value.equalsIgnoreCase(FINDELEMENTBYATTRWITHVALUEANDGETATTR.toString())) {
            return FINDELEMENTBYATTRWITHVALUEANDGETATTR;
        } else {
            log.warn("There is no HtmlObject type like: {}", value);
            return CLASS;
        }
    }

    public abstract Object getValueByParam(Element startElement, String paramName, String shopName);

}
