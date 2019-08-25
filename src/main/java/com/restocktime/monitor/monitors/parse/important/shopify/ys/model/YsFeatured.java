package com.restocktime.monitor.monitors.parse.important.shopify.ys.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YsFeatured {
    private List<YsProduct> products;

    public List<YsProduct> getProducts() {
        return products;
    }

    public void setProducts(List<YsProduct> products) {
        this.products = products;
    }
}
