package com.restocktime.monitor.monitors.parse.important.shopify.ys.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restocktime.monitor.monitors.parse.important.shopify.model.shopify.Variant;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YsProduct {
    private String url;
    private String title;
    private String i_440;
    private Boolean available;
    private Long price;
    private List<Variant> variants;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getI_440() {
        return i_440;
    }

    public void setI_440(String i_440) {
        this.i_440 = i_440;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }
}
