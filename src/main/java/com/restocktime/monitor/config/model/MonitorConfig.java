package com.restocktime.monitor.config.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restocktime.monitor.config.model.notifications.SiteNotificationsConfig;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorConfig {
    private Page[] pages;
    private Map<String, String> quicktask;
    private SiteNotificationsConfig notifications;
    private GlobalSettings globalSettings;
    private ProxyConfig proxyConfig;

    public Page[] getPages() {
        return pages;
    }

    public void setPages(Page[] pages) {
        this.pages = pages;
    }

    public Map<String, String> getQuicktask() {
        return quicktask;
    }

    public void setQuicktask(Map<String, String> quicktask) {
        this.quicktask = quicktask;
    }

    public SiteNotificationsConfig getNotifications() {
        return notifications;
    }

    public void setNotifications(SiteNotificationsConfig notifications) {
        this.notifications = notifications;
    }

    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }
}
