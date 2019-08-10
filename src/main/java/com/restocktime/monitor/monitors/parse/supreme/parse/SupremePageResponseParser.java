package com.restocktime.monitor.monitors.parse.supreme.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.supreme.attachment.SupremeBuilder;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupremePageResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SupremeAllProductResponseParser.class);

    private StockTracker stockTracker;
    private String locale;
    private final String SUP_URL_TEMPLATE = "https://www.supremenewyork.com%s";
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;

    private Pattern productPattern = Pattern.compile("<article><div class=\"inner-article\"><a style=\"height:150px;\" href=\"([^\"]*)\"><img width=\"150\" height=\"150\" src=\"([^\"]*)\" alt=\"[^\"]*\" />(?:<div class=\"sold_out_tag\">sold out</div>)?</a><h1><a class=\"name-link\" href=\"[^\"]*\">([^<]*)</a></h1><p><a class=\"name-link\" href=\"[^\"]*\">([^<]*)</a></p></div></article>");

    public SupremePageResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, String locale, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.locale = locale;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();
        Matcher productMatcher = productPattern.matcher(responseString);

        while(productMatcher.find()){
            String url = String.format(SUP_URL_TEMPLATE, productMatcher.group(1));

            if(productMatcher.group(0).contains("sold_out_tag")) {
                stockTracker.setOOS(url);
                continue;
            }

            String img = String.format("https:%s", productMatcher.group(2));
            String name = productMatcher.group(3);
            String color = productMatcher.group(4);

            if(stockTracker.notifyForObject(url, isFirst)){
                SupremeBuilder.buildAttachments(attachmentCreater, url, img, locale, name, color, "", new ArrayList<>(), formatNames);
            }

        }

    }
}
