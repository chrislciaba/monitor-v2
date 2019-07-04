package com.restocktime.monitor.monitors.parse.gemini.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.ingest.shopify.Shopify;
import com.restocktime.monitor.monitors.parse.gemini.attachment.GeminiRestockBuilder;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiRestockResponseParser  implements AbstractResponseParser {
    private StockTracker stockTracker;
    private String url;
    private Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private Pattern pidPattern = Pattern.compile("<input type=\"hidden\" name=\"product_id\" value=\"([0-9]*)\" />");
    private Pattern imgPattern = Pattern.compile("src=\"([^\\\"]*)\" alt=\"Image 1\"");
    private Pattern pricePattern = Pattern.compile("itemprop=\"price\">([^<]*)</em>");
    private String oosPattern = "<div class=\"CurrentlySoldOut\">";
    private String badUrl = "<title>ERROR: The requested URL could not be retrieved</title>";
    private String errorUrl = "502 Server Error";
    private String squidError = "The Squid Software Foundation";
    private String ATC_TEMPLATE = "http://www.geminicollectibles.net/cart.php?action=add&product_id=%s&qty[]=1";
    private List<String> formatNames;
    final static Logger logger = Logger.getLogger(Shopify.class);

    public GeminiRestockResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            logger.info("invalid");
            return;
        }

        try {

            String responseString = basicHttpResponse.getBody();

            if (responseString.contains(badUrl) || responseString.contains(errorUrl) || responseString.contains(squidError)){
                return;
            }

            if (!responseString.contains(oosPattern) && responseString.contains("http://www.geminicollectibles.net/login.php")) {
                Matcher imgMatcher = imgPattern.matcher(responseString);
                Matcher titleMatcher = titlePattern.matcher(responseString);
                Matcher priceMatcher = pricePattern.matcher(responseString);
                Matcher pidMatcher = pidPattern.matcher(responseString);
                String imgUrl = imgMatcher.find() ? imgMatcher.group(1) : null;
                String title = titleMatcher.find() ? titleMatcher.group(1) : "TITLE NOT FOUND";
                String price = "N/A";
                String atcUrl = " ";
                String pid = "";
                if (priceMatcher.find()) {
                    price = priceMatcher.group(1);
                }
                if (pidMatcher.find()) {
                    pid = pidMatcher.group(1);
                    atcUrl = String.format(ATC_TEMPLATE, pid);
                }

                if (stockTracker.notifyForObject(url, isFirst) && !title.equals("TITLE NOT FOUND")) {
                    GeminiRestockBuilder.buildAttachments(attachmentCreater, url, imgUrl, title, price, atcUrl, formatNames);

                }
            } else {
                stockTracker.setOOS(url);
                logger.info("OOS - " + url);
            }
        } catch (Exception e){

        }
    }
}
