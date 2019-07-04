package com.restocktime.monitor.monitors.parse.walmart.model.terra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer {
    private ProductAvailability productAvailability;
    private String sellerId;

    public ProductAvailability getProductAvailability() {
        return productAvailability;
    }

    public String getSellerId() { return sellerId; }

    public void setProductAvailability(ProductAvailability productAvailability) {
        this.productAvailability = productAvailability;
    }

}
