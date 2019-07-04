package com.restocktime.monitor.config.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicMonitorConfig {
    private String url;
    private String apiKey;
    private Integer numInCluster;
    private Integer totalClusterSize;
    private String mode;
    private List<Integer> tiers;

    private NotificationsFormatConfig notifications;

    private Map<String, String> webhookFormatMap;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getNumInCluster() {
        return numInCluster;
    }

    public void setNumInCluster(Integer numInCluster) {
        this.numInCluster = numInCluster;
    }

    public Integer getTotalClusterSize() {
        return totalClusterSize;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTotalClusterSize(Integer totalClusterSize) {
        this.totalClusterSize = totalClusterSize;
    }

    public NotificationsFormatConfig getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationsFormatConfig notifications) {
        this.notifications = notifications;
    }

    public Map<String, String> getWebhookFormatMap() {
        return webhookFormatMap;
    }

    public void setWebhookFormatMap(Map<String, String> webhookFormatMap) {
        this.webhookFormatMap = webhookFormatMap;
    }

    public List<Integer> getTiers() {
        return tiers;
    }
}
