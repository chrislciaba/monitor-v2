package com.restocktime.monitor.monitors.parse.oneblockdown.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.oneblockdown.model.index.ObdProduct;
import com.restocktime.monitor.monitors.parse.oneblockdown.model.index.ObdProductContainer;
import com.restocktime.monitor.monitors.parse.sns.SnsResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneBlockDownProductIndexParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SnsResponseParser.class);
    private StockTracker stockTracker;
    private Map<String, String> oosMap;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;
    private ObjectMapper objectMapper;


    String patternStr = "var preloadedItems = ([^;]*);";
    Pattern pattern = Pattern.compile(patternStr);

    public OneBlockDownProductIndexParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        oosMap = new HashMap<>();
        this.keywordSearchHelper = keywordSearchHelper;
        this.formatNames = formatNames;
        this.objectMapper = new ObjectMapper();
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        oosMap.clear();

        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        Matcher matcher = pattern.matcher(responseString);
        if(matcher.find()){
            String jsonStr = "{\"products\":" + matcher.group(1) + "}";
            try {
                ObdProductContainer obdProductContainer =
                        objectMapper.readValue(jsonStr, ObdProductContainer.class);
                for(ObdProduct obdProduct : obdProductContainer.getProducts()) {
                    if(
                            !obdProduct.getIsOutOfStock() &&
                                    stockTracker.notifyForObject(obdProduct.getPermalink(), isFirst)){
                        DefaultBuilder.buildAttachments(attachmentCreater, obdProduct.getPermalink(), null, "OBD", obdProduct.getManufacturer().getTitle() + " - " + obdProduct.getTitle(), formatNames);
                    } else if(obdProduct.getIsOutOfStock()){
                        stockTracker.setOOS(obdProduct.getPermalink());
                    }
                }
            } catch (Exception e) {

            }
        }


//        boolean found = false;
//        while(products.find()){
//            found = true;
//            String oos = products.group(1);
//            String link = products.group(2);
//            String name = products.group(3);
//            String sku = products.group(4);
//
//            System.out.println(link);
//
//            if(!oos.contains("sold-out")){
//                if(stockTracker.notifyForObject(link, isFirst)) {
//                    DefaultBuilder.buildAttachments(attachmentCreater, String.format(SNS_TEMPLATE, link), null, "OBD", name, formatNames);
//                    //    attachmentCreater.addMessages(String.format(SNS_TEMPLATE, link), (name + " - " + sku).toUpperCase(), "SNS", null, null);
//                }
//            } else {
//                stockTracker.setOOS(link);
//            }
//        }
//
//        if(found)
//            logger.info("found products");
    }
}
