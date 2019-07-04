package com.restocktime.monitor.monitors.parse.footdistrict.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.footdistrict.parse.model.FootdistricResult;
import com.restocktime.monitor.monitors.parse.footdistrict.parse.model.FootdistrictSearch;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FootdistrictParseSearchAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(FootdistrictParseSearchAbstractResponse.class);

    private StockTracker stockTracker;
    private List<String> formatNames;
    private ObjectMapper objectMapper;

    public FootdistrictParseSearchAbstractResponse(StockTracker stockTracker, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.objectMapper = new ObjectMapper();
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();
        try {
            FootdistrictSearch footdistrictSearch = objectMapper.readValue(responseString, FootdistrictSearch.class);
            for (FootdistricResult footdistricResult : footdistrictSearch.getResults()) {
                logger.info(footdistricResult.getTitle() + footdistricResult.getImage_link() + footdistricResult.getLink());
                if(stockTracker.notifyForObject(footdistricResult.getLink(), isFirst)) {
                    DefaultBuilder.buildAttachments(attachmentCreater, footdistricResult.getLink(), footdistricResult.getImage_link(), "Footdistrict", footdistricResult.getTitle(), formatNames);
                }
            }
        } catch (Exception e) {
           // e.printStackTrace();
        }
    }
}
