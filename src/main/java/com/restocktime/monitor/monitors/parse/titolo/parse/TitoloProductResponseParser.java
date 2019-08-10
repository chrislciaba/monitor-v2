package com.restocktime.monitor.monitors.parse.titolo.parse;

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

public class TitoloProductResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(TitoloProductResponseParser.class);


    String patternStr = "<title>(.*)</title>";
    Pattern pattern = Pattern.compile(patternStr);
    private String fallbackName;
    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    public TitoloProductResponseParser(String fallbackName, StockTracker stockTracker, String url, List<String> formatNames) {
        this.fallbackName = fallbackName;
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if (responseString != null && responseString.contains("productAddToCartForm.submit(this)")) {
            Matcher m = pattern.matcher(responseString);
            String productName = fallbackName;
            if (m.find()) {
                String tmpName = m.group(1).replaceAll("\\s+", " ").trim().toUpperCase();
                if(tmpName.length() > 0){
                    productName = tmpName;
                }
            }

            logger.info("In stock: " + productName);
            if (stockTracker.notifyForObject(url, isFirst)) {
                logger.info("Notify: " + productName);

                //   attachmentCreater.addMessages(url, productName, "Titolo", null, null);
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Titolo", productName, formatNames);
            }
        } else {
            logger.info("Out of stock");
        }
    }
}