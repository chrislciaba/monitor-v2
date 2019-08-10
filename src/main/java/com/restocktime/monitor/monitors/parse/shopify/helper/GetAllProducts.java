package com.restocktime.monitor.monitors.parse.shopify.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.shopify.model.api.ProductListing;
import com.restocktime.monitor.monitors.parse.shopify.model.api.ProductListings;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GetAllProducts {
    final static Logger logger = Logger.getLogger(GetAllProducts.class);
    private static final String PRODUCT_LISTINGS_TEMPLATE = "%s/api/product_listings.json?page=%s";
    
    public static Map<Long, Boolean> getExistingProductMap(HttpRequestHelper httpRequestHelper, BasicRequestClient basicRequestClient, String url){
        Map<Long, Boolean> existingProductsMap = new HashMap<>();;
        int pageCount = 1;
        ObjectMapper objectMapper = new ObjectMapper();

        if(url.contains("eflash")){
            return existingProductsMap;
        }


        while(true){
            String currentPageUrl = String.format(PRODUCT_LISTINGS_TEMPLATE, UrlHelper.deriveBaseUrl(url), Integer.toString(pageCount));


            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, currentPageUrl);
            Timeout.timeout(2500);
            if(basicHttpResponse.getResponseCode().get() == 400){
                logger.info("Password up");
                return existingProductsMap;
            }

            if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
                continue;
            } else if(basicHttpResponse.getBody().get().contains("{\"errors\":\"Internal Server Error\"}")){
                logger.error("Server error - " + currentPageUrl);
                continue;
            }

            try{
                ProductListings productListings = objectMapper.readValue(basicHttpResponse.getBody().get(), ProductListings.class);

                if(productListings.getProductListings().size() == 0){
                    break;
                }

                for(ProductListing productListing : productListings.getProductListings()){
                    existingProductsMap.put(productListing.getProductId(), productListing.getAvailable());
                }


            } catch (Exception e){

                logger.error(basicHttpResponse.getBody());
                continue;
            }

            pageCount++;
        }

        return existingProductsMap;
    }
}
