package com.restocktime.monitor.monitors.parse.footpatrol.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.footpatrol.attachment.FootpatrolBuilder;
import com.restocktime.monitor.monitors.parse.footpatrol.model.app.FpOption;
import com.restocktime.monitor.monitors.parse.footpatrol.model.app.FpProduct;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.ArrayList;
import java.util.List;


public class FootpatrolAppResponseParser implements AbstractResponseParser {

    private StockTracker stockTracker;
    private ObjectMapper objectMapper;
    private List<String> formatNames;

    public FootpatrolAppResponseParser(StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.objectMapper = new ObjectMapper();
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();
        try {
            FpProduct fpProduct = objectMapper.readValue(responseString, FpProduct.class);

            List<String> sizes = new ArrayList<>();
            for(String key : fpProduct.getOptions().keySet()){
                FpOption fpOption = fpProduct.getOptions().get(key);
                if(fpOption.getStockStatus().equals("IN STOCK")){
                    sizes.add(fpOption.getSize());
                }
            }

            if(sizes.isEmpty()){
                stockTracker.setOOS(fpProduct.getID());
                return;
            }

            if(stockTracker.notifyForObject(fpProduct.getID(), false)){
                FootpatrolBuilder.buildAttachments(attachmentCreater, fpProduct.getID(), fpProduct.getMainImage(), "Footpatrol", fpProduct.getName(), formatNames);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
