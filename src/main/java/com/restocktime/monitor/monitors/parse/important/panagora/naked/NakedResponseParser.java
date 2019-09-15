package com.restocktime.monitor.monitors.parse.important.panagora.naked;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.ops.log.DiscordLog;
import com.restocktime.monitor.util.ops.log.WebhookType;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NakedResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(NakedResponseParser.class);
    private String patternStr = "<title>(.*)</title>";
    private Pattern pattern;
    String soldOutStr = "<span class=\"ml-3\">\\s*This product is out of stock\\.\\s*</span>";
    Pattern soldOutPattern;
    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    public NakedResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        soldOutPattern = Pattern.compile(soldOutStr);
        pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains("/cart/add")){

            Matcher m = pattern.matcher(responseString);
            String productName = "PRODUCT_NAME_UNAVAILABLE";
            if (m.find()) {
                productName = m.group(1).replaceAll("\\s+"," ").trim().toUpperCase();
            }
            logger.info("In stock: " + productName);
            DiscordLog.log(WebhookType.NAKED, "In stock:" + productName);
            if(stockTracker.notifyForObject(url, isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Naked", productName, formatNames);
            }

        } else if(soldOutPattern.matcher(responseString).find()){
            DiscordLog.log(WebhookType.NAKED, "Out of stock:" + url);

            logger.info("Out of stock");
            stockTracker.setOOS(url);
        } else if(responseString.contains("Squid")){
            logger.info("Proxy dead");
        } else if(responseString.contains("Page not found")){
            DiscordLog.log(WebhookType.NAKED, "Page down:" + url);

            logger.info("Page down");
            stockTracker.setOOS(url);
        } else if(responseString.contains("<h2>Why do I have to complete a CAPTCHA?</h2>")){
            DiscordLog.log(WebhookType.NAKED, "Cap at monitor parser:" + url);

            logger.info("captcha");
        }
        else {
            String uuid = UUID.randomUUID().toString();
            DiscordLog.log(WebhookType.NAKED, "Other, search for " + uuid + ": " + url);

            logger.error(uuid + " " + responseString);
        }
    }
}
