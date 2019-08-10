package com.restocktime.monitor.monitors.parse.patta;

import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PattaProductResponseParser implements AbstractResponseParser {
    private Pattern titlePattern = Pattern.compile("<title>(.*)</title>");

    private StockTracker stockTracker;
    private List<String> formatNames;
    private String url;

    public PattaProductResponseParser(String url, StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }


        if (basicHttpResponse.getBody().get().contains("product-addtocart-button") && stockTracker.notifyForObject(url, isFirst)){
            Matcher titleMatcher = titlePattern.matcher(basicHttpResponse.getBody().get());
            String title = "HYPE DIRECT LINK SHIT CLICK THIS";
            if(titleMatcher.find())
                title = titleMatcher.group(1);
            DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Patta", title, formatNames);
        } else if(basicHttpResponse.getBody().get().contains("<title>404 Not Found</title>") || (basicHttpResponse.getBody().get().contains("stock unavailable") && !basicHttpResponse.getBody().get().contains("product-addtocart-button"))){
            stockTracker.setOOS(url);
        }
    }
}
