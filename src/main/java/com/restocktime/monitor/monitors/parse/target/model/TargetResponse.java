package com.restocktime.monitor.monitors.parse.target.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetResponse {
    private List<TargetProduct> products;
    private TargetProduct product;

    public TargetProduct getProduct() { return product; }

    public List<TargetProduct> getProducts() {
        return products;
    }

    public void setProducts(List<TargetProduct> products) {
        this.products = products;
    }
}
