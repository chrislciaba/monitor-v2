package com.restocktime.monitor.monitors.parse.offwhite.parse;

import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OffWhiteSearchAbstractResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(OffWhiteSearchAbstractResponseParser.class);


    String urlPatternStr = "<article[^>]*data-json-url='([^']*)'";
    Pattern urlPattern = Pattern.compile(urlPatternStr);
    String namePatternStr = "<div class='brand-name'>([^>]*)</div>";
    Pattern namePattern = Pattern.compile(namePatternStr);
    private StockTracker stockTracker;
    private String baseUrl;
    private List<String> formatNames;

    public OffWhiteSearchAbstractResponseParser(StockTracker stockTracker, String baseUrl, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.baseUrl = baseUrl;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        Matcher nameMatcher = namePattern.matcher(responseString);
        Matcher urlMatcher = urlPattern.matcher(responseString);
        String name, url;
        while(nameMatcher.find() && urlMatcher.find()){
            name = nameMatcher.group(1).trim();
            url = urlMatcher.group(1);
            url = url.substring(0, url.length() - 5);
            if(stockTracker.notifyForObject(url, isFirst)){
              //  attachmentCreater.addMessages(baseUrl + url, name, "Off White", null, null);
                logger.info("In stock: " + baseUrl + url);
            }
        }
    }
}
