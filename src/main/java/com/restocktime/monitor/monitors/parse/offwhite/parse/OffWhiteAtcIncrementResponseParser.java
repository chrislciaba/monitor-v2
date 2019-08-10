package com.restocktime.monitor.monitors.parse.offwhite.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OffWhiteAtcIncrementResponseParser {
    private final Pattern cartPattern = Pattern.compile("<a href=\"([^\"]*)\"><img src=\"([^\"]*)\" alt=\"[^\"]*\" /></a></div><div class='cart-items-description'><span class='prod-title'>([^<]*)</span>");
    private StockTracker stockTracker;
    private final String OFF_WHITE_URL_TEMPLATE = "https://off---white.com%s";
    private ObjectMapper objectMapper;
    private List<String> formatNames;
    private String url;
    private String size;

    final static Logger logger = Logger.getLogger(OffWhiteAtcIncrementResponseParser.class);


    public OffWhiteAtcIncrementResponseParser(String size, StockTracker stockTracker, ObjectMapper objectMapper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.objectMapper = objectMapper;
        this.formatNames = formatNames;
        this.size = size;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><").replaceAll("\n", "");
        Matcher cartMatcher = cartPattern.matcher(responseString);

        if(cartMatcher.find() && stockTracker.notifyForObject("offwhite", false)){
            logger.info("carted");
            String img = cartMatcher.group(2);
            String name =  "[ATC]" + cartMatcher.group(3) + " (" + size + ")";
            if(!name.contains("SOCKS"))
                DefaultBuilder.buildAttachments(attachmentCreater, UrlHelper.urlRandNumberAppended(url), img, "Off White", name, formatNames);
            else{
                while(cartMatcher.find()){
                    img = cartMatcher.group(2);
                    name =  "[ATC]" + cartMatcher.group(3) + " (" + size + ")";
                    if(!name.contains("SOCKS")) {
                        DefaultBuilder.buildAttachments(attachmentCreater, UrlHelper.urlRandNumberAppended(url), img, "Off White", name, formatNames);
                        break;
                    }
                }
            }
        } else if(responseString.contains("not available")){
            logger.info("OFF---WHITE OOS");
            stockTracker.setOOS("offwhite");
        } else if(basicHttpResponse.getResponseCode().get() == 403){
            logger.info("forbidden");
        } else if(responseString.contains("<h1>Internal server error (500)</h1>")){
            logger.info("500 SERVER ERROR");
        } else if(responseString.contains("<h1>The page cannot be found (404)</h1>")) {
            logger.info("404 NOT FOUND");
        } else if(responseString.contains("<div class='cart empty-cart'>")){
            logger.info("Cart empty, not an error");

        }
        else {
            logger.info(responseString);
            logger.info(basicHttpResponse.getResponseCode());
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
