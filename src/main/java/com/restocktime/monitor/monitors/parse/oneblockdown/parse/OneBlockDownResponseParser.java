package com.restocktime.monitor.monitors.parse.oneblockdown.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.oneblockdown.model.index.ObdResponse;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;

public class OneBlockDownResponseParser {
    final static Logger logger = Logger.getLogger(ShopifyAbstractResponseParser.class);

    private StockTracker stockTracker;
    private List<String> formatNames;
    private ObjectMapper objectMapper;
    private String curSku;



    public OneBlockDownResponseParser(StockTracker stockTracker, ObjectMapper objectMapper, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.objectMapper = objectMapper;
    }


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null)
            return;
        if(basicHttpResponse.getBody().get().contains("\"success\":false")){
            stockTracker.setOOS(curSku);
            return;
        }

        try {
            ObdResponse obdResponse = objectMapper.readValue(basicHttpResponse.getBody().get(), ObdResponse.class);
            if(stockTracker.notifyForObject(curSku, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, obdResponse.getPayload().get(0).getPermalink(), obdResponse.getPayload().get(0).getImageObject().getImageUrl(), "OBD", obdResponse.getPayload().get(0).getTitle(), formatNames);
            }
        } catch (Exception e){

        }
    }

    public void setCurSku(String sku){
        this.curSku = sku;
    }
}
