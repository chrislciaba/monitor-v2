package com.restocktime.monitor.monitors.parse.nittygritty;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NittyGrittyAbstractResponseParser implements AbstractResponseParser {

    private final String patternStr = "class=\"product-item__link\" href=\"([^\"]*)\" [^>]*data-ecommerce-name=\"([^\"]*)\"";
    private final String FORMAT_URL = "https://www.nittygrittystore.com%s";
    private Pattern pattern;
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;

    public NittyGrittyAbstractResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();
        responseString = responseString.replaceAll("\\s+", " ");
        Matcher m = pattern.matcher(responseString);
        while(m.find()) {
            String l = m.group(1);
            String name = m.group(2).replaceAll("\\s+", " ");
            if (keywordSearchHelper.search(name) && stockTracker.notifyForObject(l, isFirst)) {
               // attachmentCreater.addMessages(String.format(FORMAT_URL, l), name, "NittyGritty", null, null, null);
                DefaultBuilder.buildAttachments(attachmentCreater, String.format(FORMAT_URL, l), null, "Nitty Gritty", name, formatNames);
            }
        }
    }
}
