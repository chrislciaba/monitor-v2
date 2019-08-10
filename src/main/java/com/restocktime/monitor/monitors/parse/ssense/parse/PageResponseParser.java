package com.restocktime.monitor.monitors.parse.ssense.parse;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.ingest.ssense.Ssense;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class PageResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    final static Logger logger = Logger.getLogger(Ssense.class);

    public PageResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        String titleStr = "<title>(.*)</title>";
        Pattern titlePattern = Pattern.compile(titleStr);
        String skuStr = "class=\"product-sku\">([^<]*)</span>";
        Pattern skuPattern = Pattern.compile(skuStr);
        String title = "TITLE_UNAVAILABLE";


        try {
            Matcher m = titlePattern.matcher(responseString);
            if (responseString.contains("<strong>Not available</strong>") || responseString.contains("<strong>Sold Out</strong>")) {
                logger.info("Not live yet or oos");
            } else if (responseString.contains("<span class=\"button-label\">Add to bag</span>")) {
                logger.info("IN STOCK - " + url);
                if (m.find()) {
                    title = m.group(1).replaceAll("\\| SSENSE", "").trim();
                } else {
                    title = url;
                }

                if (stockTracker.notifyForObject(url, isFirst)) {
                    DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Ssense", title, formatNames);
                }
            }
        } catch (Exception e) {
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }
}