package com.restocktime.monitor.monitors.parse.footpatrol.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.footpatrol.attachment.FootpatrolBuilder;
import com.restocktime.monitor.monitors.parse.footpatrol.model.app.FpOption;
import com.restocktime.monitor.monitors.parse.footpatrol.model.app.FpProduct;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;


public class FootpatrolAppResponseParser implements AbstractResponseParser {
    private static final Logger log = Logger.getLogger(FootpatrolAppResponseParser.class);

    private StockTracker stockTracker;
    private ObjectMapper objectMapper;
    private List<String> formatNames;

    public FootpatrolAppResponseParser(StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.objectMapper = new ObjectMapper();
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

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
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }

    }
}
