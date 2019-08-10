package com.restocktime.monitor.monitors.parse.walgreens;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalgreensSearchResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private Pattern pattern = Pattern.compile("<a title='Shop now' href='/([^']*)'><img src=\"([^\"]*)\" title=\"\" alt=\"\" /></a></section><section class=\"mt15 wag-dolp-lsprice\">([^<]*)</section>");
    private List<String> formatNames;

    public WalgreensSearchResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().replaceAll(">\\s+<", "><");

        Matcher productMatcher = pattern.matcher(responseString);
        while(productMatcher.find()){
            String url = productMatcher.group(1);
            String imgUrl = productMatcher.group(2);
            String name = productMatcher.group(3);

            if(stockTracker.notifyForObject(url, isFirst) && keywordSearchHelper.search(name)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, imgUrl, "Walgreens", name, formatNames);
            }
        }
    }
}
