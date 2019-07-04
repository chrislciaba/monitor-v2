package com.restocktime.monitor.monitors.parse.shopify.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.model.api.ProductListing;
import com.restocktime.monitor.monitors.parse.shopify.model.api.ProductListings;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.*;

import static com.restocktime.monitor.monitors.parse.shopify.attachment.ShopifyBuilder.buildAttachments;

public class ShopifyProductListingsResponseParser implements AbstractResponseParser {


    final static Logger logger = Logger.getLogger(ShopifyProductListingsResponseParser.class);

    private String storeName;
    private String url;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private List<String> unfilteredFormatNames;
    private Map<Long, Boolean> products;
    private KeywordSearchHelper keywordSearchHelper;



    public ShopifyProductListingsResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, String url, List<String> formatNames){
        try {
            URL storeUri = new URL(url);
            this.storeName = "";
        } catch(Exception e){
            this.storeName = "";
        }
        this.url = url;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
        unfilteredFormatNames = new ArrayList<>();
        for(String format : formatNames) {
            if(format.contains("unfiltered")) {
                unfilteredFormatNames.add(format);
            }
        }
    }


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (basicHttpResponse == null) {
            return;
        } else if (basicHttpResponse.getResponseCode() == 404 || basicHttpResponse.getResponseCode() == 401) {
            logger.info("Not loaded yet - " + url);
            return;
        } else if (basicHttpResponse.getBody() == null) {
            return;
        } if(basicHttpResponse.getResponseCode() == 400){
            logger.info("Pass up");
            return;
        } else if(basicHttpResponse.getBody().contains("Page temporarily unavailable")){
            logger.info("page unavailable " + url);
            return;
        }

        String responseString = basicHttpResponse.getBody();


        if (responseString == null) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductListings productListings = objectMapper.readValue(basicHttpResponse.getBody(), ProductListings.class);
            for(ProductListing productListing : productListings.getProductListings()){
                if(productListing.getAvailable()){
                    String imgUrl = productListing.getImages().isEmpty() ? "" : productListing.getImages().get(0).getSrc();

                    String toSearch = productListing.getProductId() + " " + productListing.getHandle() + " " + productListing.getTitle() + " " + String.join(" ", productListing.getTags().split(",")) + " " + (imgUrl==null?"":imgUrl);

                    for(Variant v : productListing.getVariants()){
                        toSearch += " " + v.getTitle();
                    }

                    if(stockTracker.notifyForObject(Long.toString(productListing.getProductId()), false) &&
                    (
                            products.containsKey(productListing.getProductId()) && !products.get(productListing.getProductId()
                            ) ||
                                    !products.containsKey(productListing.getProductId())
                    )

                    ){

                        String price = "-1";
                        List<Variant> variants = new ArrayList<>();
                        for (Variant variant : productListing.getVariants()) {
                            price = variant.getPrice();
                            if (variant.getAvailable()) {
                                variants.add(variant);
                            }
                        }

                        products.put(productListing.getProductId(), true);

                        String prodUrl = UrlHelper.deriveBaseUrl(url) + "/products/" + productListing.getHandle();


                        String keywordHit = keywordSearchHelper.searchAndReturnMatch(toSearch);

                        if(keywordSearchHelper.search(toSearch) && keywordHit != null && keywordHit.length() > 0){
                            buildAttachments(attachmentCreater, prodUrl, imgUrl, productListing.getTitle(), price, variants, keywordHit, formatNames, 2, Optional.empty(), Optional.empty());
                            logger.info("kw hit");

                        } else if(unfilteredFormatNames.size() > 0){
                            buildAttachments(attachmentCreater, prodUrl, imgUrl, productListing.getTitle(), price, variants, "", unfilteredFormatNames, 2, Optional.empty(), Optional.empty());
                        }

                    }
                } else if(!productListing.getAvailable()) {
                    products.put(productListing.getProductId(),false);

                    stockTracker.setOOS(Long.toString(productListing.getProductId()));
                } else {
                    //logger.info("Found but in stock " + productListing.getHandle());
                }
            }

        } catch (Exception e){

        }
    }

    public void setProducts(Map<Long, Boolean> products){
        this.products = products;
    }
}
