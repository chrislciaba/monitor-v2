package com.restocktime.monitor.monitors.parse.complexcon.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.complexcon.model.MarketPlace;
import com.restocktime.monitor.monitors.parse.complexcon.model.ReserveObject;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;


public class ComplexconResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(ComplexconResponseParser.class);
    private StockTracker stockTracker;
    private ObjectMapper objectMapper;
    private List<String> formatNames;


    public ComplexconResponseParser(StockTracker stockTracker, ObjectMapper objectMapper, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.objectMapper = objectMapper;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();


        try {
            ReserveObject reserveObject = objectMapper.readValue(responseString, ReserveObject.class);
            for(MarketPlace marketPlace : reserveObject.getData()){
                if(stockTracker.notifyForObject(Integer.toString(marketPlace.getId()), isFirst)){
                    DefaultBuilder.buildAttachments(attachmentCreater, null, null,"Complexcon Reserves", marketPlace.getTitle(), formatNames);

                }

            }

            if(attachmentCreater.isEmpty()){
                logger.info("Nothing found");
            }

        } catch (Exception e){

        }

    }
}
