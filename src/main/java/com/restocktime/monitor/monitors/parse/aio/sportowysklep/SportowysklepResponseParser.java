package com.restocktime.monitor.monitors.parse.aio.sportowysklep;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SportowysklepResponseParser implements AbstractResponseParser {
    private static final Pattern PATTERN = Pattern.compile("<a href=\"([^\"]*)\" class=\"text-center\" title=\"([^\"]*)\"><img src=\"[^\"]*\" data-echo=\"([^\"]*)\"");

    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    public SportowysklepResponseParser(StockTracker stockTracker, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");

        Matcher m = PATTERN.matcher(responseString);

        while (m.find()) {
            if (stockTracker.notifyForObject(m.group(1), isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, m.group(1), m.group(3), "Dude I cannot spell the name just click", m.group(2), formatNames);
            }
        }
    }
}
