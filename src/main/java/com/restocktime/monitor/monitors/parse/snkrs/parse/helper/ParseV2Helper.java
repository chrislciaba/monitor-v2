package com.restocktime.monitor.monitors.parse.snkrs.parse.helper;

import com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv2.Node;
import com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv2.ProductFeed;
import com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv2.ProductObject;
import com.restocktime.monitor.monitors.parse.snkrs.model.ParsedResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParseV2Helper {

    private ProductFeed productFeed;

    public ParseV2Helper(ProductFeed productFeed) {
        this.productFeed = productFeed;
    }

    public String getId(ProductObject productObject){
        return productObject.getId();
    }

    public String getName(ProductObject productObject){
        productObject.getPublishedContent().getProperties().getSeo().getTitle();
        if(
                productObject == null ||
                productObject.getPublishedContent() == null ||
                productObject.getPublishedContent().getProperties() == null ||
                productObject.getPublishedContent().getProperties().getSeo() == null ||
                productObject.getPublishedContent().getProperties().getSeo().getTitle() == null) {

            return "PRODUCT_NAME_UNAVAILABLE";
        }

        return productObject.getPublishedContent().getProperties().getSeo().getTitle();
    }

    public String getSlug(ProductObject productObject){
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

    public String getType(ProductObject productObject){
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




    public String getImage(ProductObject productObject){
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

    public ParsedResponse getParsedResponse(ProductObject productObject){
        return new ParsedResponse(getId(productObject), getName(productObject), getSlug(productObject), getType(productObject), getImage(productObject), getSelectionEngine(productObject), DateHelper.formatDate(getStartEntryDate(productObject)), DateHelper.getEpochTimeSecs(getStartEntryDate(productObject)), getSku(productObject));
    }

    public List<ParsedResponse> getParsedResponseList(){
        List<ParsedResponse> parsedResponses = new ArrayList<>();
        for(ProductObject productObject : productFeed.getObjects()){
            parsedResponses.add(getParsedResponse(productObject));
        }

        return parsedResponses;
    }


}
