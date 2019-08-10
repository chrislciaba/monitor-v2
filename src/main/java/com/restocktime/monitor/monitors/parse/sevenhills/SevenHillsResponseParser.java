package com.restocktime.monitor.monitors.parse.sevenhills;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SevenHillsResponseParser implements AbstractResponseParser {
    private Pattern p = Pattern.compile("<a href=\"([^\"]*)\" title=\"([^\"]*)\" class=\"product-image\"");
    private StockTracker stockTracker;
    private List<String> formatNames;

    public SevenHillsResponseParser(StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;

        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }
        String responseString = basicHttpResponse.getBody().get();
        Matcher m = p.matcher(responseString);

        while (m.find()) {
            if (stockTracker.notifyForObject(m.group(1), isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, m.group(1), null, "7hills", m.group(2), formatNames);
            }
        }
    }
}
