package com.restocktime.monitor.monitors.parse.important.footsites.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.footsites.helper.SkuHelper;
import com.restocktime.monitor.monitors.parse.important.footsites.model.FootsitesProduct;
import com.restocktime.monitor.monitors.parse.important.footsites.model.VariantAttribute;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class FootsitesResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(FootsitesResponseParser.class);

    private String storeName;
    private String url;
    private String name;
    private String sku;
    private StockTracker stockTracker;
    private List<String> formatNames;


    public FootsitesResponseParser(StockTracker stockTracker, String url, String name, List<String> formatNames){
        this.storeName = UrlHelper.getHost(url);
        this.url = url;
        this.name = name;
        this.sku = SkuHelper.getSku(url);
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();



        if(responseString.contains("The product you are trying to view is no longer available")){
            logger.info("The product you are trying to view is no longer available");
            stockTracker.setOOS(url);
            return;
        } else if(responseString.contains("<TITLE>Access Denied</TITLE>")){
            logger.info("Access Denied");

            return;
        } else if(responseString.contains("\"errors\"")){
            logger.info("error - " + basicHttpResponse.getBody());
            return;
        }


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            FootsitesProduct footsitesProduct = objectMapper.readValue(responseString, FootsitesProduct.class);
            for(VariantAttribute variantAttribute : footsitesProduct.getVariantAttributes()){
                if(variantAttribute.getSku() != null && variantAttribute.getSku().equals(sku) && variantAttribute.getStockLevelStatus() != null){
                    if(stockTracker.notifyForObject(url, isFirst) && variantAttribute.getStockLevelStatus().equals("inStock")){
                        DefaultBuilder.buildAttachments(attachmentCreater, url, null, storeName, name, formatNames);
                    } else if(!variantAttribute.getStockLevelStatus().equals("inStock")){
                        logger.info("OOS - " + sku);
                        stockTracker.setOOS(url);
                    }
                }
            }
        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }
}
