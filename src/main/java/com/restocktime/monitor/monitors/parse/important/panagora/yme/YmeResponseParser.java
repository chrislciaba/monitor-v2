package com.restocktime.monitor.monitors.parse.important.panagora.yme;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YmeResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(YmeResponseParser.class);


    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    String patternStr = "<title>(.*)</title>";
    Pattern pattern = Pattern.compile(patternStr);
    String soldOutStr = "class=\"btn\\s+product-form-button\\s+is-unavailable\\s+is-disabled\"";
    Pattern soldOutPattern = Pattern.compile(soldOutStr);

    public YmeResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains("IP blocked")){
            logger.info("IP blocked");
            return;
        }
        else if(responseString != null && responseString.contains("class=\"btn product-form-button\"")){

            Matcher m = pattern.matcher(responseString);
            String productName = "";
            if (m.find()) {
                productName = m.group(1).replaceAll("\\s+"," ").trim().toUpperCase();
            }
            logger.info("In stock: " + productName);
            if(stockTracker.notifyForObject(url, isFirst)) {
              //  attachmentCreater.addMessages(url, productName, "YME", null, null);
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "YME", productName, formatNames);
            }

        } else if(soldOutPattern.matcher(responseString).find()){
            logger.info("Out of stock");
            stockTracker.setOOS(url);
        } else if(responseString.contains("/cdn-cgi/l/chk_captcha")) {
            logger.error("Cap");
        }
    }

}
