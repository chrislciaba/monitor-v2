package com.restocktime.monitor.monitors.parse.funko.target.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String buy_url;
    private ProductDescription product_description;
    private Enrichment enrichment;

    public String getBuy_url() {
        return buy_url;
    }

    public ProductDescription getProduct_description() {
        return product_description;
    }

    public Enrichment getEnrichment() {
        return enrichment;
    }
}
