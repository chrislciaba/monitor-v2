package com.restocktime.monitor.monitors.parse.supreme.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.ingest.ssense.Ssense;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.supreme.model.supreme.*;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class SupremeAllProductResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SupremeAllProductResponseParser.class);

    private StockTracker stockTracker;
    private String url;
    private String name;
    private String locale;
    private final String SUP_URL_TEMPLATE = "https://www.supremenewyork.com/shop/%s/%s";
    private KeywordSearchHelper keywordSearchHelper;
    private  List<String> formatNames;

    public SupremeAllProductResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, String locale, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.name = name;
        this.locale = locale;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        if(responseString.contains("<!DOCTYPE html>"))
            return;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MobileStock mobileStock = objectMapper.readValue(responseString, MobileStock.class);
            String id;
            for(String key : mobileStock.getCategories().keySet()){
                for(AllProduct product : mobileStock.getCategories().get(key)){
                    id = Integer.toString(product.getId());
                    if(stockTracker.notifyForObject(id, false)){

                        DefaultBuilder.buildAttachments(attachmentCreater, String.format(SUP_URL_TEMPLATE, product.getCategory_name().replaceAll("/", "_").toLowerCase(), id), "https:" + product.getImage_url(), "Supreme", "[" + locale + "] " + product.getName(), formatNames);
                    }
                }
            }

        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);

        }

    }
}
