package com.restocktime.monitor.monitors.parse.disney.parse;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisneyRestockResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private String url;
    private final String availabilityStr = "\"availability\":\"InStock\"";
    private final String oosStr = "\"availability\":\"SoldOut\"";
    private final Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private List<String> formatNames;

    public DisneyRestockResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");

        if(responseString.contains(availabilityStr)){
            Matcher titleMatcher = titlePattern.matcher(responseString);
            String title = "N/A";
            if(titleMatcher.find()){
                title = titleMatcher.group(1);
            }

            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Disney", title, formatNames);
            }
        } else if(responseString.contains(oosStr)){
            stockTracker.setOOS(url);
        }
    }
}
