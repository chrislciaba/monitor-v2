package com.restocktime.monitor.monitors.parse.naked;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ParseNakedAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(ParseNakedAbstractResponse.class);
    private String patternStr = "<title>(.*)</title>";
    private Pattern pattern;
    String soldOutStr = "<span class=\"ml-3\">\\s*Sold out\\s*</span>";
    Pattern soldOutPattern;
    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    public ParseNakedAbstractResponse(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        soldOutPattern = Pattern.compile(soldOutStr);
        pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        logger.error("HELLO");

        try {
            throw new RuntimeException("HEEEHEEHE");
        } catch (Exception e) {
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        }

        String responseString = basicHttpResponse.getBody();

        if(responseString.contains("/cart/add")){

            Matcher m = pattern.matcher(responseString);
            String productName = "PRODUCT_NAME_UNAVAILABLE";
            if (m.find()) {
                productName = m.group(1).replaceAll("\\s+"," ").trim().toUpperCase();
            }
            logger.info("In stock: " + productName);
            if(stockTracker.notifyForObject(url, isFirst)) {
            //    attachmentCreater.addMessages(url, productName, "Naked", null, null);
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Naked", productName, formatNames);
            }

        } else if(soldOutPattern.matcher(responseString).find()){
            logger.info("Out of stock");
            stockTracker.setOOS(url);
        } else if(responseString.contains("Squid")){
            logger.info("Proxy dead");
        } else if(responseString.contains("Page not found")){
            logger.info("Page down");
            stockTracker.setOOS(url);
        } else if(responseString.contains("<h2>Why do I have to complete a CAPTCHA?</h2>")){
            logger.info("captcha");
        }
        else {
            logger.error(responseString);
        }
    }
}
