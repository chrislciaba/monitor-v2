package com.restocktime.monitor.monitors.parse.bstn.parse;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BSTNParseProductAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(BSTNParseProductAbstractResponse.class);
    private StockTracker stockTracker;
    private String url;
    private final String patternStr = "<title>(.*)</title>";
    private Pattern pattern;
    private List<String> formatNames;

    public BSTNParseProductAbstractResponse(StockTracker stockTracker, String url, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        responseString = responseString.toLowerCase();

        boolean containsATCButton = responseString.contains("submitaddtocart");

        if (containsATCButton) {
            Matcher m = pattern.matcher(responseString);
            String productName = "PRODUCT_NAME_UNAVAILABLE";
            if (m.find()) {
                productName = m.group(1).trim().toUpperCase();
            }

            logger.info("Found product: " + productName);

            if (stockTracker.notifyForObject(url, isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "BSTN", productName, formatNames);
            }

        } else if (responseString.contains("maximum number")) {
            logger.info("Max visitors");
        } else if (responseString.contains("<span>sold out</span>")) {
            logger.info("OOS");
            stockTracker.setOOS(url);
        } else {
            logger.info("OOS - " + url);

        }
    }
}
