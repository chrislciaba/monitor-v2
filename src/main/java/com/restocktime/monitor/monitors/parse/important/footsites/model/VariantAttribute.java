package com.restocktime.monitor.monitors.parse.important.footsites.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VariantAttribute {
    private String sku;
    private String stockLevelStatus;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStockLevelStatus() {
        return stockLevelStatus;
    }

    public void setStockLevelStatus(String stockLevelStatus) {
        this.stockLevelStatus = stockLevelStatus;
    }
}
