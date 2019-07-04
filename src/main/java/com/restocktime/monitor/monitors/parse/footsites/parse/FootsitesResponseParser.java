package com.restocktime.monitor.monitors.parse.footsites.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.footsites.helper.SkuHelper;
import com.restocktime.monitor.monitors.parse.footsites.model.FootsitesProduct;
import com.restocktime.monitor.monitors.parse.footsites.model.VariantAttribute;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;

public class FootsitesResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(FootsitesResponseParser.class);

    private String storeName;
    private String url;
    private String name;
    private String sku;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private DiscordLog discordLog;


    public FootsitesResponseParser(StockTracker stockTracker, String url, String name, List<String> formatNames){
        this.storeName = UrlHelper.getHost(url);
        this.url = url;
        this.name = name;
        this.sku = SkuHelper.getSku(url);
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.discordLog = new DiscordLog(FootsitesResponseParser.class);
    }


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {


        if (basicHttpResponse == null) {
            return;
        } else if (basicHttpResponse.getBody() == null) {
            return;
        } else if(basicHttpResponse.getBody().contains("The product you are trying to view is no longer available")){
            logger.info("N/A");
            stockTracker.setOOS(url);
            return;
        } else if(basicHttpResponse.getBody().contains("<TITLE>Access Denied</TITLE>")){
            discordLog.error(FootsitesResponseParser.class.getName() + ": Banned on footsites " + url);
            return;
        } else if(basicHttpResponse.getBody().contains("\"errors\"")){
            logger.info("error - " + basicHttpResponse.getBody());
            return;
        }

        String responseString = basicHttpResponse.getBody();

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

        }
    }
}
