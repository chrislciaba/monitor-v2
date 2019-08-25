package com.restocktime.monitor.monitors.parse.funko.amazon;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonResponseParser implements AbstractResponseParser {
    private final String atc = "add-to-cart-button";
    private final String oos = "This item is only available from third-party sellers";

    private final Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");

    private StockTracker stockTracker;
    private List<String> formatNames;
    private String url;

    public AmazonResponseParser(String url, StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains(atc)){
            Matcher titleMatcher = titlePattern.matcher(responseString);
            String name = titleMatcher.find()?titleMatcher.group(1):"N/A";
            String img = null;

            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, img, "Amazon", name, formatNames);
            }
        } else if(responseString.contains(oos)){
            stockTracker.setOOS(url);
        }
    }
}
