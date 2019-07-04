package com.restocktime.monitor.monitors.parse.solebox;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoleboxResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SoleboxResponseParser.class);
    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    String patternStr = "<title>([^<]*)</title>";
    Pattern pattern = Pattern.compile(patternStr);

    public SoleboxResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){

        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }
        String responseString = basicHttpResponse.getBody().toLowerCase();

        if (responseString.contains("in den warenkorb") || responseString.contains("add to basket") || responseString.contains("add to cart")) {
            Matcher m = pattern.matcher(responseString);

            String productName = "PRODUCT_NAME_UNAVAILABLE";
            if (m.find()) {
                productName = m.group(1).trim().toUpperCase();
            }
            logger.info("In stock: " + productName);
            if(stockTracker.notifyForObject(url, isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Solebox", productName, formatNames);
             //   attachmentCreater.addMessages(url, productName, "Solebox", null, null);
            }
        } else if(responseString.contains("429 - too many requests") || responseString.contains("too mamy requests") || responseString.contains("leider zu viele anfragen")) {
            logger.info("Banned");
        } else if(basicHttpResponse.getResponseCode() >= 400){
            logger.info("Solebox error code" + basicHttpResponse.getResponseCode());
        } else {
            logger.info("out of stock");
            stockTracker.setOOS(url);
        }
    }

}
