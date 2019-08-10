package com.restocktime.monitor.monitors.parse.funimation;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunimationResponseParser implements AbstractResponseParser {
    private final String addtocartpresent = "<meta property=\"og:availability\" content=\"In Stock\">";
    private final String notAvailable = "<meta property=\"og:availability\" content=\"Out of Stock\">";
    private final Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private final Pattern imgPattern = Pattern.compile("<meta property=\"og:image\" content=\"([^\"]*)\"");

    private StockTracker stockTracker;
    private List<String> formatNames;
    private String url;

    public FunimationResponseParser(String url, StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains(addtocartpresent)){
            Matcher titleMatcher = titlePattern.matcher(responseString);
            Matcher imgMatcher = imgPattern.matcher(responseString);
            String name = titleMatcher.find()?titleMatcher.group(1):"N/A";
            String img = imgMatcher.find()?imgMatcher.group(1):null;

            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, img, "Funimation", name, formatNames);
            }
        } else if(responseString.contains(notAvailable)){
            stockTracker.setOOS(url);
        }
    }
}
