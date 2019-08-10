package com.restocktime.monitor.monitors.parse.svd.parse;

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

public class SvdProductResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SvdProductResponseParser.class);
    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    String patternStr = "<title>(.*)</title>";
    Pattern pattern = Pattern.compile(patternStr);

    public SvdProductResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if (responseString.contains("productAddToCartForm.submit(this)")) {
            Matcher m = pattern.matcher(responseString);
            String productName = "";
            if (m.find()) {
                productName = m.group(1).replaceAll("\\s+", " ").replaceAll("Buy now", "").replaceAll("&[A-Za-z]*;", "").trim().toUpperCase();
            }

            logger.info("In stock: " + productName);
            if (stockTracker.notifyForObject(url, isFirst)) {
               // attachmentCreater.addMessages(url, productName, "SVD", null, null);
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "SVD", productName, formatNames);
            }

        } else if(responseString.contains("class=\"sold-out\"")) {
            logger.info("Out of stock");
            stockTracker.setOOS(url);
        } else if(responseString.contains("<strong>Not available</strong>")){
            logger.info("not live yet");
        } else if(responseString.contains("403 Forbidden")) {
            logger.info("403 Forbidden");
        } else {
            logger.info("error - " + url);
        }
    }
}
