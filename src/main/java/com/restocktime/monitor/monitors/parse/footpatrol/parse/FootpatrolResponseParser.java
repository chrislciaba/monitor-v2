package com.restocktime.monitor.monitors.parse.footpatrol.parse;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FootpatrolResponseParser implements AbstractResponseParser {

    private StockTracker stockTracker;
    private String url;
    private String name;
    private Pattern pattern = Pattern.compile("title=\"Select Size ([^\"]*)\"");
    private List<String> formatNames;

    public FootpatrolResponseParser(StockTracker stockTracker, String url, String name, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.name = name;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        Matcher sizeMatcher = pattern.matcher(responseString);
        boolean isFirstSize = true;
        String sizes = "";
        while(sizeMatcher.find()){

            String size = sizeMatcher.group(1);
            if(!isFirstSize){
                sizes = sizes + ", ";
            } else {
                isFirstSize = false;
            }
            sizes = sizes + size;
        }

        if(sizes.length() > 0){
            if(stockTracker.notifyForObject(url, isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Footpatrol Front End", name, formatNames);
            }
        } else if(sizes.length() == 0){
            stockTracker.setOOS(url);
        }
    }
}
