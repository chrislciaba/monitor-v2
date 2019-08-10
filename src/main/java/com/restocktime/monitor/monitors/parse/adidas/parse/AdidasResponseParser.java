package com.restocktime.monitor.monitors.parse.adidas.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.adidas.model.Availability;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;

public class AdidasResponseParser implements AbstractResponseParser {
    final static Logger log = Logger.getLogger(AdidasResponseParser.class);

    private String url;
    private String imgUrl;
    private final String URL_TEMPLATE = "https://www.adidas.com/us/%s.html";
    private StockTracker stockTracker;
    private List<String> formatNames;

    private String name;


    public AdidasResponseParser(StockTracker stockTracker, String sku, String imgUrl, String name, List<String> formatNames){
        this.url = String.format(URL_TEMPLATE, sku);
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.name = name;
        this.imgUrl = imgUrl;
    }


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) throws Exception {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        ObjectMapper objectMapper = new ObjectMapper();
        Availability availability = objectMapper.readValue(responseString, Availability.class);
        if(availability.getAvailability_status() != null && availability.getAvailability_status().equals("IN_STOCK")){
            log.info("IN STOCK");
            if(stockTracker.notifyForObject(url, isFirst))
                DefaultBuilder.buildAttachments(attachmentCreater, url, imgUrl, "Adidas", name, formatNames);
        } else if(availability.getAvailability_status() != null && availability.getAvailability_status().equals("NOT_AVAILABLE")){
            stockTracker.setOOS(url);
        }
    }
}
