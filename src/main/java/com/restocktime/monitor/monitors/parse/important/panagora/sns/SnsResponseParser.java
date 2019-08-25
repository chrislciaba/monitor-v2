package com.restocktime.monitor.monitors.parse.important.panagora.sns;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SnsResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(SnsResponseParser.class);
    private StockTracker stockTracker;
    private Map<String, String> oosMap;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;

    private final String SNS_TEMPLATE = "https://www.sneakersnstuff.com%s";
    private String searchUrl;
    String s = "><span class=\"ribbon__text\"></span></span><h3 class=\"card__title\"><a itemprop=\"url\" href=\"/en/product/35915/nike-air-max-180-hi-ambush\" class=\"card__link\"><span class=\"card__brand\">NikeLab</span><strong itemprop=\"name\" class=\"card__name\">Air Max 180 Hi / Ambush</strong>";
    String newPatternStr = "(<span class=\"ribbon__text\">[^<]*</span></span>)?<h3 class=\"card__title\"><a itemprop=\"url\" href=\"([^\"]*)\" class=\"card__link\"><span class=\"card__brand\">([^<]*)</span><strong itemprop=\"name\" class=\"card__name\">([^<]*)</strong>";
  //  String s = "<span class=\"ribbon__text\">Sold out</span></span><h3 class=\"card__title\"><a itemprop=\"url\" href=\"/en/product/38383/adidas-yeezy-boots\" class=\"card__link\"><span class=\"card__brand\">adidas Originals x Kanye West</span><strong itemprop=\"name\" class=\"card__name\">Yeezy Boots</strong></a>"
    String patternStr = "<a class=\"row product([^\"]*)\" data-id=\"[0-9]*\" href=\"([^\"]*)\" data-gtm-list-product='\\{\"name\":\"([^\"]*)\",\"id\":\"([^\"]*)\"";
    Pattern pattern = Pattern.compile(newPatternStr);

    public SnsResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, String searchUrl, List<String> formatNames) {
        this.stockTracker = stockTracker;
        oosMap = new HashMap<>();
        this.keywordSearchHelper = keywordSearchHelper;
        this.searchUrl = searchUrl;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        oosMap.clear();

        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");
        Matcher products = pattern.matcher(responseString);
        boolean found = false;
        while(products.find()){
            found = true;
            String oos = products.group(1);
            oos = oos == null ? null : oos.trim();
            String link = products.group(2);
            String brand = products.group(3);
            String name = products.group(4);

            if(oos == null){
                if(stockTracker.notifyForObject(link, isFirst)) {
                    DefaultBuilder.buildAttachments(attachmentCreater, String.format(SNS_TEMPLATE, link), null, "SNS", brand + " " + name, formatNames);
                    //discordLog.info("@here found some shit " + link);
                }
            } else {
                stockTracker.setOOS(link);
            }
        }
    }
}
