package com.restocktime.monitor.monitors.parse.aio.sneakerbarber;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SneakerbarberResponseParser implements AbstractResponseParser {
    private final Pattern PATTERN = Pattern.compile("href=\"https://www\\.sneakerbarber\\.com/en/produkt/([^/]*)/\">");

    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    public SneakerbarberResponseParser(StockTracker stockTracker, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        Matcher m = PATTERN.matcher(responseString);

        while (m.find()) {
            String handle = m.group(1);
            if (stockTracker.notifyForObject(handle, isFirst)) {


                DefaultBuilder.buildAttachments(attachmentCreater, "https://www.sneakerbarber.com/en/produkt/" + handle, null, "Sneakerbarber", handle.replaceAll("-", " ").toUpperCase(), formatNames);
            }
        }
    }
}
