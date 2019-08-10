package com.restocktime.monitor.monitors.parse.shopify.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.ShopifyJson;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.restocktime.monitor.monitors.parse.shopify.attachment.ShopifyBuilder.buildAttachments;

public class ShopifyAbstractResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(ShopifyAbstractResponseParser.class);

    private String storeName;
    private String url;
    private StockTracker stockTracker;
    private List<String> formatNames;

    private final int MAX_COUNT = 100;
    private int count;
    private int errors;
    private int bans;


    public ShopifyAbstractResponseParser(StockTracker stockTracker, String url, List<String> formatNames){
        try {
            URL storeUri = new URL(url);
            this.storeName = "";
        } catch(Exception e){
            this.storeName = "";
        }
        this.url = url;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;

        count = 0;
        errors = 0;
        bans = 0;
    }


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(count == MAX_COUNT){
            count = 0;
            bans = 0;
            errors = 0;
        }

        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        count++;

        if (basicHttpResponse == null) {
            return;
        } else if (basicHttpResponse.getResponseCode().get() == 404 || basicHttpResponse.getResponseCode().get() == 401) {
            stockTracker.setOOS(url);
            return;
        } else if (basicHttpResponse.getBody() == null) {
            return;
        } else if(basicHttpResponse.getBody().get().contains("Page temporarily unavailable<")){
            bans++;
            return;
        } else if(basicHttpResponse.getResponseCode().get() == 430){
            bans++;
            return;
        }
        String responseString = basicHttpResponse.getBody().get();

        if (responseString == null) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ShopifyJson shopifyJson = objectMapper.readValue(responseString, ShopifyJson.class);
            if (shopifyJson.getAvailable()) {
                logger.info("In Stock: " + shopifyJson.getTitle());
                List<Variant> variants = new ArrayList<>();
                for (Variant variant : shopifyJson.getVariants()) {
                    if (variant.getAvailable()) {
                        if(url.contains("yeezysupply.com")){
                            variant.setTitle(variant.getOption1());
                        }
                        variants.add(variant);
                    }
                }


                if(stockTracker.notifyForObject(shopifyJson.getHandle(), isFirst)) {
                    logger.info("Sending: " + shopifyJson.getTitle());

                    buildAttachments(attachmentCreater, UrlHelper.deriveBaseUrl(url) + "/products/" + shopifyJson.getHandle(), "https:" + shopifyJson.getFeatured_image(), shopifyJson.getTitle().trim(), Integer.toString(shopifyJson.getPrice() / 100), variants, "Link", formatNames, 0, Optional.empty(), Optional.empty());
                }
            } else if (!shopifyJson.getAvailable()) {
                stockTracker.setOOS(shopifyJson.getHandle());
            } else {
                logger.info(responseString);

            }
        } catch (Exception e){
            errors++;

        }
    }

}
