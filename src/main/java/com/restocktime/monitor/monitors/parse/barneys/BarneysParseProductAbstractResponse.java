package com.restocktime.monitor.monitors.parse.barneys;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BarneysParseProductAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(BarneysParseProductAbstractResponse.class);


    private StockTracker stockTracker;
    private String url;
    private final String PATTERN_STR = "<input type=\"hidden\" id=\"fp_availableSizes\" value='[([^]]*)]' />";
    private Pattern pattern;
    private String name;
    private List<String> formatNames;

    public BarneysParseProductAbstractResponse(StockTracker stockTracker, String url, String name, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        this.pattern = Pattern.compile(PATTERN_STR);
        this.name = name;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();
        Matcher matcher = pattern.matcher(responseString);
        if(matcher.find()) {
            String[] sizes = matcher.group(1).split(",");

            if (sizes.length > 0 && stockTracker.notifyForObject(url, isFirst)){
               // attachmentCreater.addMessages(url, name, "Barneys", null, null);
                logger.info("In stock: " + name);
            } else {
                logger.info("OOS or don't notify: " + name);

            }

        } else if(responseString.contains("No Color and size varidation template")){
            logger.info("Not loaded");
            stockTracker.setOOS(url);
        }  else {
            logger.info(responseString);
        }
    }
}
