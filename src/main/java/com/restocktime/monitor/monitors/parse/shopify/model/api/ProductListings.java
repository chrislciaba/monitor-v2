package com.restocktime.monitor.monitors.parse.shopify.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductListings {
    @JsonProperty(value = "product_listings")
    private List<ProductListing> productListings;

    public List<ProductListing> getProductListings() {
        return productListings;
    }

    public void setProductListings(List<ProductListing> productListings) {
        this.productListings = productListings;
    }
}
