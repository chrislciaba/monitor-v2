package com.restocktime.monitor.monitors.parse.backdoor.parse;

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

public class BackdoorSearchResponseParser implements AbstractResponseParser {

    private final String SKU_TEMPLATE = "<span class=\"sku\" itemprop=\"sku\">%s</span>";
    private final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*)</title>");
    private final Pattern LINK_PATTERN = Pattern.compile("<link rel=\"canonical\" href=\"([^\"]*)\" />");
    private final Pattern COOKIE_PATTERN = Pattern.compile("document.cookie\\s*=\\s*\"([^\"]*)\"");

    final static Logger logger = Logger.getLogger(BackdoorSearchResponseParser.class);


    private String sku;
    private String url;
    private StockTracker stockTracker;
    private List<String> formatNames;

    public BackdoorSearchResponseParser(String sku, String url, StockTracker stockTracker, List<String> formatNames) {
        this.sku = sku;
        this.url = url;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        if(responseString.contains(String.format(SKU_TEMPLATE, sku))){
            Matcher name = TITLE_PATTERN.matcher(responseString);
            Matcher link = LINK_PATTERN.matcher(responseString);
            String productName = sku, productLink = url;
            if(name.find()){
                productName = name.group(1);
            }

            if(link.find()){
                productLink = link.group(1);
            }
            if(stockTracker.notifyForObject(productLink, isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, productLink, null, "Backdoor", productName, formatNames);
            }
        }
    }

    public String getAndSetCookie(BasicHttpResponse basicHttpResponse) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return null;
        }


        String responseString = basicHttpResponse.getBody().get();
        Matcher cookie = COOKIE_PATTERN.matcher(responseString);
        if(cookie.find()){
            logger.info("Found cookie");
            logger.info(cookie.group(1));
            return cookie.group(1);
        }

        return null;
    }

}
