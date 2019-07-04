package com.restocktime.monitor.monitors.parse.disney.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisneySitemapResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private String url;
    private Pattern pattern = Pattern.compile("<loc>https://www\\.shopdisney\\.com/([^<]*)</loc>");
    private List<String> formatNames;

    public DisneySitemapResponseParser(StockTracker stockTracker, String url, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
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
            String handle = productMatcher.group(1);
            String name = String.join(" ",handle.split("-")).toUpperCase();

            if(stockTracker.notifyForObject(handle, isFirst) && keywordSearchHelper.search(name)){
                DefaultBuilder.buildAttachments(attachmentCreater, String.format("https://www.shopdisney.com/%s", url), null, "Disney", name, formatNames);
            }
        }
    }
}
