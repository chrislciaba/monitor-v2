package com.restocktime.monitor.monitors.parse.thenextdoor;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TheNextDoorResponseParser implements AbstractResponseParser {

    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;
    String patternStr = "<a class=\"product-name\" href=\"([^?]*)\\?[^\"]*\" title=\"([^\"]*)\" itemprop=\"url\" >";
    Pattern pattern = Pattern.compile(patternStr);

    public TheNextDoorResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        responseString = responseString.replaceAll(">\\s+<", "><");
        Matcher m = pattern.matcher(responseString);
        while(m.find()) {
            String l = m.group(1);
            String name = m.group(2).replaceAll("\\s+", " ");
            if (stockTracker.notifyForObject(l, isFirst) && keywordSearchHelper.search(name)) {
                DefaultBuilder.buildAttachments(attachmentCreater, l, null, "The Next Door", name, formatNames);
            }
        }
    }

}
