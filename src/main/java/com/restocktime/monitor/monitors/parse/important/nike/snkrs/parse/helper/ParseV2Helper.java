package com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.helper;

import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.SizeAndStock;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2.*;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.ParsedResponse;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ParseV2Helper {
    final static Logger logger = Logger.getLogger(ParseV2Helper.class);



    public String getId(ProductObject productObject){
        return productObject.getId();
    }

    private String getName(ProductObject productObject){
        productObject.getPublishedContent().getProperties().getSeo().getTitle();
        if(
                productObject != null &&
                productObject.getPublishedContent() != null &&
                productObject.getPublishedContent().getProperties() != null){
            if (productObject.getPublishedContent().getProperties().getSeo() != null &&
                    productObject.getPublishedContent().getProperties().getSeo().getTitle() != null) {
                return productObject.getPublishedContent().getProperties().getSeo().getTitle();
            } else if (productObject.getPublishedContent().getProperties().getTitle() != null && productObject.getPublishedContent().getProperties().getSubtitle() != null) {
                return productObject.getPublishedContent().getProperties().getTitle() + " " + productObject.getPublishedContent().getProperties().getSubtitle();
            }
        }

        return "PRODUCT_NAME_UNAVAILABLE";
    }

    private String getSlug(ProductObject productObject){
        productObject.getPublishedContent().getProperties().getSeo().getTitle();
        if(
                productObject == null ||
                        productObject.getPublishedContent() == null ||
                        productObject.getPublishedContent().getProperties() == null ||
                        productObject.getPublishedContent().getProperties().getSeo() == null ||
                        productObject.getPublishedContent().getProperties().getSeo().getSlug() == null) {

            return "";
        }

        return productObject.getPublishedContent().getProperties().getSeo().getSlug();
    }

    private String getType(ProductObject productObject){
        if( productObject == null ||
                productObject.getPublishedContent() == null ||
                productObject.getPublishedContent().getProperties() == null ||
                productObject.getPublishedContent().getProperties().getCustom() == null ||
                productObject.getPublishedContent().getProperties().getCustom().getTags() == null
                ){
            return null;
        }

        for(String property : productObject.getPublishedContent().getProperties().getCustom().getTags()){
            if(property.equals("SNKRS PASS")){
                return "SNKRS PASS";
            } else if(property.equals("BEHIND THE DESIGN")){
                return "BEHIND THE DESIGN";
            }
        }

        return null;
    }

    private boolean getAvailable(ProductObject productObject){
        try {
            for(AvailableSku availableSku : productObject.getProductInfo().get(0).getAvailableSkus()){
                if (availableSku.getAvailable()) {
                    return true;
                }
            }

            return false;

        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        }

        return true; //would rather notify than not
    }




    private String getImage(ProductObject productObject){
        if(
        productObject == null ||
                productObject.getPublishedContent() == null ||
                productObject.getPublishedContent().getNodes() == null ||
                !(productObject.getPublishedContent().getNodes() instanceof List)){
            return null;
        }

        for(Node node : productObject.getPublishedContent().getNodes()){
            if(node.getProperties() != null && node.getProperties().getPortraitURL() != null){
                try{
                    URL url = new URL(node.getProperties().getPortraitURL());
                    return node.getProperties().getPortraitURL();
                } catch (MalformedURLException e){
                    logger.error(EXCEPTION_LOG_MESSAGE, e);
                }
            }
        }

        return null;
    }

    private String getSku(ProductObject productObject){
        if(productObject.getProductInfo() != null &&
                productObject.getProductInfo().get(0) != null &&
                productObject.getProductInfo().get(0).getMerchProduct() != null &&
                productObject.getProductInfo().get(0).getMerchProduct().getStyleColor() != null
                ){

            return productObject.getProductInfo().get(0).getMerchProduct().getStyleColor();
        } else {
            return null;
        }
    }

    private String getSelectionEngine(ProductObject productObject){
        if(
                productObject.getProductInfo() != null &&
                productObject.getProductInfo().size() > 0 &&
                productObject.getProductInfo().get(0).getLaunchView() != null &&
                productObject.getProductInfo().get(0).getLaunchView().getMethod() != null
                ) {
            return productObject.getProductInfo().get(0).getLaunchView().getMethod();
        }

        return null;
    }

    private String getStartEntryDate(ProductObject productObject){
        if(
                productObject.getProductInfo() != null &&
                        productObject.getProductInfo().size() > 0 &&
                        productObject.getProductInfo().get(0).getLaunchView() != null &&
                        productObject.getProductInfo().get(0).getLaunchView().getStartEntryDate() != null
                ) {
            return productObject.getProductInfo().get(0).getLaunchView().getStartEntryDate();
        }

        return null;
    }

    private List<SizeAndStock> availableSizes(ProductObject productObject) {
        Map<String, String> availMap = new HashMap<>();
        List<SizeAndStock> sizes = new ArrayList<>();
        if (
                productObject.getProductInfo() != null &&
                        !productObject.getProductInfo().isEmpty() &&
                        productObject.getProductInfo().get(0).getSkus() != null &&
                        productObject.getProductInfo().get(0).getAvailableSkus() != null
        ) {
            for (Sku sku : productObject.getProductInfo().get(0).getSkus()) {
                availMap.put(sku.getId(), sku.getMerchGroup() + " " + sku.getNikeSize());
            }

            for (AvailableSku availableSku : productObject.getProductInfo().get(0).getAvailableSkus()) {
                if (availableSku.getAvailable()) {
                    sizes.add(SizeAndStock.builder()
                            .size(availMap.get(availableSku.getId()))
                            .stock(availableSku.getLevel())
                            .build());
                }
            }
        }

        return sizes;

    }

    public ParsedResponse getParsedResponse(ProductObject productObject){
        return ParsedResponse.builder()
                .id(getId(productObject))
                .name(getName(productObject))
                .slug(getSlug(productObject))
                .type(getType(productObject))
                .image(getImage(productObject))
                .selectionEngine(getSelectionEngine(productObject))
                .launchDate(DateHelper.formatDate(getStartEntryDate(productObject)))
                .launchDateEpoch(DateHelper.getEpochTimeSecs(getStartEntryDate(productObject)))
                .sku(getSku(productObject))
                .available(getAvailable(productObject))
                .sizeAndStocks(availableSizes(productObject))
                .build();
    }

    public List<ParsedResponse> getParsedResponseList(ProductFeed productFeed){
        List<ParsedResponse> parsedResponses = new ArrayList<>();
        for(ProductObject productObject : productFeed.getObjects()){
            parsedResponses.add(getParsedResponse(productObject));
        }

        return parsedResponses;
    }


}
