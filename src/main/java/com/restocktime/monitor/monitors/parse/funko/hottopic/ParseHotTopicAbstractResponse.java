package com.restocktime.monitor.monitors.parse.funko.hottopic;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHotTopicAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(ParseHotTopicAbstractResponse.class);

    private StockTracker stockTracker;
    private String url;
    private final String patternStr = "<title>(.*)</title>";
    private Pattern pattern;
    private List<String> formatNames;

    public ParseHotTopicAbstractResponse(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        logger.info(basicHttpResponse.getBody());

        if(basicHttpResponse.getBody().get().contains("add-to-cart\">Add to Bag")){
            logger.info("in stock");
            String name = "PRODUCT_NAME_UNAVAILABLE";

            if(stockTracker.notifyForObject(url, isFirst)){
                Matcher patternMatcher = pattern.matcher(basicHttpResponse.getBody().get());
                if(patternMatcher.find()){
                    name = patternMatcher.group(1);
                }
      //          attachmentCreater.addMessages(url, name, "Hot Topic", null, null);
            }

        } else if(basicHttpResponse.getBody().get().contains("disabled=\"disabled\">Add to Bag")){
            logger.info("oos");
            stockTracker.setOOS(url);
        }
        else if(basicHttpResponse.getResponseCode().get() >= 400){
            logger.info("Bad response");
        } else {
            logger.info("broken");
        }
    }
}
