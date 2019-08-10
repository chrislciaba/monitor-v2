package com.restocktime.monitor.monitors.parse.rimowa;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RimowaResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(RimowaResponseParser.class);
    private StockTracker stockTracker;
    private String url;
    private final String namePatternStr = "<title>([^<]*)</title>";
    private Pattern pattern;
    private List<String> formatNames;

    public RimowaResponseParser(StockTracker stockTracker, String url, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        pattern = Pattern.compile(namePatternStr);
        this.formatNames = formatNames;

    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        String name = "NAME_UNAVAILABLE";

        if(responseString.contains("add-to-cart-sticky")){
            Matcher m = pattern.matcher(responseString);
            logger.info("in-stock");
            if(m.find()){
                name = m.group(1);
            }
            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Rimowa", name, formatNames);
             //   attachmentCreater.addMessages(url, name, "Rimowa", null, null);
            }
        } else if(responseString.contains("product-availability\">Coming soon</span>")){
            logger.info("Coming soon - " + url);
            stockTracker.setOOS(url);
        } else if(responseString.contains("product-availability\">Out of stock</span>")){
            logger.info("oos - " + url);
            stockTracker.setOOS(url);
        }
    }
}
