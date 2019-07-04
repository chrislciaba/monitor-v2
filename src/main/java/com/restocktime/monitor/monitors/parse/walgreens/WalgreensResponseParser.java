package com.restocktime.monitor.monitors.parse.walgreens;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalgreensResponseParser implements AbstractResponseParser {
    private final String notAvailable = "This product is no longer available on our site";
    private final String available = "product-atc-mob-btn";

    private StockTracker stockTracker;
    private String url;
    private Pattern titlePattern = Pattern.compile("<title>([^<])</title>");
    private Pattern imgPattern = Pattern.compile("<meta property=\"og:image\" content=\"([^\"]*)\"");
    private List<String> formatNames;

    public WalgreensResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();


        if(responseString.contains(available)){
            Matcher imgMatcher = imgPattern.matcher(responseString);
            Matcher titleMatcher = titlePattern.matcher(responseString);
            String imgUrl = imgMatcher.find()?imgMatcher.group(1):null;
            String title = titleMatcher.find()?titleMatcher.group(1):"TITLE NOT FOUND";


            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, imgUrl, "Gemini", title, formatNames);

            }

        } else if(responseString.contains(notAvailable)) {
            stockTracker.setOOS(url);
        }
    }
}
