package com.restocktime.monitor.monitors.parse.aio.suppastore;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuppaStoreResponseParser implements AbstractResponseParser {
    private final Pattern PATTERN = Pattern.compile("<a href=\"([^\"]*)\" class=\"product photo product-item-photo\" tabindex=\"-1\"><span class=\"product-image-container\"><span class=\"product-image-wrapper\"><img class=\"product-image-photo\"\\s+src=\"([^\"]*)\"\\s+width=\"[^\"]*\"\\s+height=\"[^\"]*\"\\s+alt=\"([^\"]*)\"/>");
    private StockTracker stockTracker;
    private List<String> formatNames;

    public SuppaStoreResponseParser(StockTracker stockTracker, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll("\n", "").replaceAll(">\\s+<", "><");
        Matcher m = PATTERN.matcher(responseString);

        while (m.find()) {
            if (stockTracker.notifyForObject(m.group(1), isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, m.group(1), m.group(2), "Suppastore", m.group(3), formatNames);
            }
        }
    }
}
