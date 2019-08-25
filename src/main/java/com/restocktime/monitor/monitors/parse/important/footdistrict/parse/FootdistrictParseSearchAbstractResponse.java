package com.restocktime.monitor.monitors.parse.important.footdistrict.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.footdistrict.parse.model.FootdistricResult;
import com.restocktime.monitor.monitors.parse.important.footdistrict.parse.model.FootdistrictSearch;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

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
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        try {
            FootdistrictSearch footdistrictSearch = objectMapper.readValue(responseString, FootdistrictSearch.class);
            for (FootdistricResult footdistricResult : footdistrictSearch.getResults()) {
                logger.info(footdistricResult.getTitle() + footdistricResult.getImage_link() + footdistricResult.getLink());
                if(stockTracker.notifyForObject(footdistricResult.getLink(), isFirst)) {
                    DefaultBuilder.buildAttachments(attachmentCreater, footdistricResult.getLink(), footdistricResult.getImage_link(), "Footdistrict", footdistricResult.getTitle(), formatNames);
                }
            }
        } catch (Exception e) {
           logger.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }
}