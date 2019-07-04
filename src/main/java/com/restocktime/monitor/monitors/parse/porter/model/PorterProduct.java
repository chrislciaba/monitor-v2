package com.restocktime.monitor.monitors.parse.porter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PorterProduct {
    private List<PorterSku> skus;
    private String name;
    private String analyticsKey;
    private Integer id;

    public List<PorterSku> getSkus() {
        return skus;
    }

    public void setSkus(List<PorterSku> skus) {
        this.skus = skus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnalyticsKey() {
        return analyticsKey;
    }

    public void setAnalyticsKey(String analyticsKey) {
        this.analyticsKey = analyticsKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
