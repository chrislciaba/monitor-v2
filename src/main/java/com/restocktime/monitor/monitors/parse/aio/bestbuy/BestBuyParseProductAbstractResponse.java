package com.restocktime.monitor.monitors.parse.aio.bestbuy;

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

public class BestBuyParseProductAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(BestBuyParseProductAbstractResponse.class);

    private StockTracker stockTracker;
    private String patternStr = "<title>(.*)</title>";
    private final Pattern imgPattern = Pattern.compile("<meta property=\"og:image\" content=\"([^\"]*)\">");
    private Pattern pattern;
    private String url;
    private List<String> formatNames;

    public BestBuyParseProductAbstractResponse(StockTracker stockTracker, String url, List<String> formatNames){
        this.stockTracker = stockTracker;
        pattern = Pattern.compile(patternStr);
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        if(basicHttpResponse.getBody().get().contains("\"availability\":\"http://schema.org/InStock\"")){
            logger.info("in stock");
            String name = "PRODUCT_NAME_UNAVAILABLE";
            if(stockTracker.notifyForObject(url, isFirst)){
                Matcher patternMatcher = pattern.matcher(basicHttpResponse.getBody().get());
                if(patternMatcher.find()){
                    name = patternMatcher.group(1);
                }
                Matcher imgMatcher = imgPattern.matcher(basicHttpResponse.getBody().get());
                String img = imgMatcher.find()?imgMatcher.group(1):null;
                DefaultBuilder.buildAttachments(attachmentCreater, url, img, "Best Buy", name, formatNames);
            }

        } else if(basicHttpResponse.getBody().get().contains("\"availability\":\"http://schema.org/SoldOut\"")){
            logger.info("oos");
            stockTracker.setOOS(url);
        }
        else if(basicHttpResponse.getResponseCode().get() >= 400){
        } else {
            logger.info("broken");
        }
    }
}
