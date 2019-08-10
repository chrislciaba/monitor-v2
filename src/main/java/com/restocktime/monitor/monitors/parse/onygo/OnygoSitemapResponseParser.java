package com.restocktime.monitor.monitors.parse.onygo;

import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnygoSitemapResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(OnygoSitemapResponseParser.class);
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;

    Pattern pattern = Pattern.compile("<loc>([^<]*)</loc>");
    Pattern titlePattern = Pattern.compile("/([^/]*)/prod");

    public OnygoSitemapResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        String responseString = basicHttpResponse.getBody().get();

        Matcher products = pattern.matcher(responseString);
        boolean found = false;
        while(products.find()){
            found = true;
            String link = products.group(1);
            String splitLink = String.join(" ", String.join(" ", link.split("/")).split("-"));
            String title = link;
            Matcher titleMatcher = titlePattern.matcher(link);
            if(titleMatcher.find())
                title = String.join(" ", titleMatcher.group(1).split("-")).toUpperCase();

            if(keywordSearchHelper.search(splitLink)){

                if(stockTracker.notifyForObject(link, isFirst)) {
                    DefaultBuilder.buildAttachments(attachmentCreater, link, null, "Onygo", title, formatNames);
                }
            }
        }

        if(!found){
            logger.info("ERROR - " + responseString);
        } else {
            logger.info("running fine");
        }

    }
}
