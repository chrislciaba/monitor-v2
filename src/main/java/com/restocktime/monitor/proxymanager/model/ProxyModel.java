package com.restocktime.monitor.proxymanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyModel {
    Map<String, ProxyList> proxies;

    public Map<String, ProxyList> getProxies() {
        return proxies;
    }

    public void setProxies(Map<String, ProxyList> proxies) {
        this.proxies = proxies;
    }
}
