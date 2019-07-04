package com.restocktime.monitor.config.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalSettings {
    private String shopifyExtraProxies;
    private String apiKeys;
    private Map<String, String> snsMap;
    private String accessKey;
    private String secretKey;
    private String defaultShopifyKw;
    private Map<String, Double> conversionRates;
    private Long updated_at;

    public String getShopifyExtraProxies() {
        return shopifyExtraProxies;
    }

    public void setShopifyExtraProxies(String shopifyExtraProxies) {
        this.shopifyExtraProxies = shopifyExtraProxies;
    }

    public String getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(String apiKeys) {
        this.apiKeys = apiKeys;
    }

    public Map<String, String> getSnsMap() {
        return snsMap;
    }

    public void setSnsMap(Map<String, String> snsMap) {
        this.snsMap = snsMap;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, Double> conversionRates) {
        this.conversionRates = conversionRates;
    }

    public String getDefaultShopifyKw() {
        return defaultShopifyKw;
    }

    public void setDefaultShopifyKw(String defaultShopifyKw) {
        this.defaultShopifyKw = defaultShopifyKw;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }
}
