package com.restocktime.monitor.monitors.parse.bhvideo;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BhVideoParseAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(BhVideoParseAbstractResponse.class);


    private StockTracker stockTracker;
    private String url;
    private final String patternStr = "<title>(.*)</title>";
    private Pattern pattern;
    private List<String> formatNames;

    public BhVideoParseAbstractResponse(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String name = "";

        if(basicHttpResponse.getBody().get().contains("type=\"submit\">Add to Cart</button>")){
            logger.info("In stock");

            if(stockTracker.notifyForObject(url, isFirst)){
                Matcher m = pattern.matcher(basicHttpResponse.getBody().get());
                if(m.find()){
                    name = m.group(1);
                }

              //  attachmentCreater.addMessages(url, name, "BhVideo", null, null);
                //Notifications.send(discordWebhook, slackObj, attachmentCreater);
            }

        } else if(basicHttpResponse.getResponseCode().get() >= 400){
            logger.info("Error");
        } else {
            logger.info("oos - " + name);
            stockTracker.setOOS(url);
        }
    }
}
