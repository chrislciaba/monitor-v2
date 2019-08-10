package com.restocktime.monitor.monitors.parse.walmart.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.walmart.model.SearchResults;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;

public class WalmartResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(WalmartResponseParser.class);

    private ObjectMapper objectMapper;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private String pid;

    public WalmartResponseParser(ObjectMapper objectMapper, StockTracker stockTracker, List<String> formatNames, String pid) {
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.pid = pid;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        try {

            SearchResults searchResults = objectMapper.readValue(basicHttpResponse.getBody().get(), SearchResults.class);
            if(searchResults.getItems().isEmpty()){
                logger.info("OOS - " + pid);
                stockTracker.setOOS(pid);
            } else if(stockTracker.notifyForObject(pid, isFirst)) {
               String title = searchResults.getItems().get(0).getTitle();
               String img = null;
               if(!searchResults.getItems().get(0).getImages().isEmpty()){
                   img = searchResults.getItems().get(0).getImages().get(0).getUrl();
               }
               DefaultBuilder.buildAttachments(attachmentCreater, String.format("https://www.walmart.com/ip/%s", pid), img, "Walmart", title, formatNames);
            }

        } catch (Exception e){
        }
    }
}
