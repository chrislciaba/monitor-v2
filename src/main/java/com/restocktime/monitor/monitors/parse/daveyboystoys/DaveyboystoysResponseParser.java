package com.restocktime.monitor.monitors.parse.daveyboystoys;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaveyboystoysResponseParser implements AbstractResponseParser {
    private final Pattern productPattern = Pattern.compile("<a href=\"([^\"]*)\" class=\"thumbnail-image\"><img src=\"([^\"]*)\" class=\"product-image\" alt=\"([^\"]*)\"");

    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;

    public DaveyboystoysResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");

        Matcher productMatcher = productPattern.matcher(responseString);
        while(productMatcher.find()){
            String link = productMatcher.group(1);
            String productImgHandle = productMatcher.group(2);
            String name = productMatcher.group(3);

            if(stockTracker.notifyForObject(link, isFirst) && keywordSearchHelper.search(name)){
                DefaultBuilder.buildAttachments(attachmentCreater, link, String.format("https://www.daveyboystoys.com.au%s", productImgHandle), "Daveyboystoys", name, formatNames);
            }
        }
    }
}
