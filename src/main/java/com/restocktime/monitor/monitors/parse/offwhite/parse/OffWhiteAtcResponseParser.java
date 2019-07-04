package com.restocktime.monitor.monitors.parse.offwhite.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.monitors.parse.offwhite.model.offwhiteatc.AtcResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class OffWhiteAtcResponseParser {
    private StockTracker stockTracker;
    private final String OFF_WHITE_URL_TEMPLATE = "https://off---white.com%s";
    private ObjectMapper objectMapper;
    private List<String> formatNames;
    private String url;

    final static Logger logger = Logger.getLogger(OffWhiteAtcResponseParser.class);


    public OffWhiteAtcResponseParser(String url, StockTracker stockTracker, ObjectMapper objectMapper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.objectMapper = objectMapper;
        this.formatNames = formatNames;
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {
            return;
        }

        String responseString = basicHttpResponse.getBody();


        AtcResponse atcResponse = null;

        try{
            atcResponse = objectMapper.readValue(responseString, AtcResponse.class);
        } catch(IOException e){

        }

        logger.info(responseString);
        logger.info(basicHttpResponse.getResponseCode());

        if(basicHttpResponse.getResponseCode() == 200 && atcResponse != null &&  stockTracker.notifyForObject("offwhite", false)){
            String name = atcResponse.getCart().getLine_items().get(0).getDesigner_name() + " " + atcResponse.getCart().getLine_items().get(0).getName();
            String img;
            try{
                img = atcResponse.getCart().getLine_items().get(0).getImage_url();
            } catch (Exception e){
                img = null;
            }
            DefaultBuilder.buildAttachments(attachmentCreater, url, img, "Off White", name, formatNames);
        } else if(responseString.contains("not available")){
            logger.info("oos");
            stockTracker.setOOS("offwhite");
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
