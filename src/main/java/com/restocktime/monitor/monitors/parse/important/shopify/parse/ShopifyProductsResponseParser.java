package com.restocktime.monitor.monitors.parse.important.shopify.parse;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.shopify.model.products.Product;
import com.restocktime.monitor.monitors.parse.important.shopify.model.products.ShopifyProducts;
import com.restocktime.monitor.monitors.parse.important.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.*;

import static com.restocktime.monitor.monitors.parse.important.shopify.attachment.ShopifyBuilder.buildAttachments;

public class ShopifyProductsResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(ShopifyProductsResponseParser.class);
    private final int MAX_COUNT = 100;
    private int count;
    private int errors;
    private int bans;

    private String storeName;
    private String url;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private List<String> unfilteredFormatNames;
    private Map<Long, Boolean> productMap;
    private KeywordSearchHelper keywordSearchHelper;
    private boolean parsedSuccessfully;


    public ShopifyProductsResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, String url, List<String> formatNames) {
        try {
            URL storeUri = new URL(url);
            this.storeName = "";
        } catch (Exception e) {
            this.storeName = "";
        }
        this.url = url;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
        unfilteredFormatNames = new ArrayList<>();
        for (String format : formatNames) {
            if (format.contains("unfiltered")) {
                unfilteredFormatNames.add(format);
            }
        }
        productMap = new HashMap<>();
        parsedSuccessfully = false;
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

        count++;
        if (basicHttpResponse == null) {
            return;
        } else if (basicHttpResponse.getResponseCode().get() == 404 || basicHttpResponse.getResponseCode().get() == 401) {
            logger.info("Not loaded yet - " + url);
            return;
        } else if (basicHttpResponse.getBody() == null) {
            return;
        }
        if (basicHttpResponse.getResponseCode().get() == 400) {
            logger.info("Pass up");
            return;
        } else if (basicHttpResponse.getBody().get().contains("Page temporarily unavailable")) {
            logger.info("page unavailable " + url);
            bans++;
            return;
        } else if(basicHttpResponse.getResponseCode().get() == 430){
            bans++;
            logger.info("too many reqs");
            return;
        }

        String responseString = basicHttpResponse.getBody().get();


        if (responseString == null) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ShopifyProducts shopifyProducts = objectMapper.readValue(basicHttpResponse.getBody().get(), ShopifyProducts.class);
            for (Product product : shopifyProducts.getProducts()) {

                boolean available = false;
                String imgUrl = product.getImages().isEmpty() ? "" : product.getImages().get(0).getSrc();

                String toSearch = product.getId() + " " + product.getHandle() + " " + product.getTitle() + " " + String.join(" ",product.getTags()) + " " + (imgUrl == null ? "" : imgUrl);



                for (Variant v : product.getVariants()) {
                    toSearch += " " + v.getTitle();
                    if (v.getAvailable()) {
                        available = true;
                    }
                }

                if(!parsedSuccessfully){
                    productMap.put(product.getId(), available);
                }

                if (available) {
                    if (stockTracker.notifyForObject(Long.toString(product.getId()), false) &&
                            (
                                    productMap.containsKey(product.getId()) && !productMap.get(product.getId()
                                    ) ||
                                            !productMap.containsKey(product.getId())
                            )

                            ) {

                        String price = "-1";
                        List<Variant> variants = new ArrayList<>();
                        for (Variant variant : product.getVariants()) {
                            price = variant.getPrice();
                            if (variant.getAvailable()) {
                                variants.add(variant);
                            }
                        }

                        productMap.put(product.getId(), true);



                        String prodUrl = UrlHelper.deriveBaseUrl(url) + "/products/" + product.getHandle();


                        String keywordHit = keywordSearchHelper.searchAndReturnMatch(toSearch);

                        if (keywordSearchHelper.search(toSearch) && keywordHit != null && keywordHit.length() > 0) {
                            buildAttachments(attachmentCreater, prodUrl, imgUrl, product.getTitle(), price, variants, keywordHit, formatNames, 1, Optional.empty(), Optional.empty());
                            logger.info("kw hit");

                        } else if (unfilteredFormatNames.size() > 0) {
                            buildAttachments(attachmentCreater, prodUrl, imgUrl, product.getTitle(), price, variants, "", unfilteredFormatNames, 1, Optional.empty(), Optional.empty());
                        }


                    }
                } else if (!available) {
                    //logger.info("Found but oos " + product.getHandle());
                    productMap.put(product.getId(), false);

                    stockTracker.setOOS(Long.toString(product.getId()));
                } else {
                    //logger.info("Found but in stock " + product.getHandle());
                }

            }
            parsedSuccessfully = true;

        } catch(Exception e){
            errors++;

        }
    }

}

