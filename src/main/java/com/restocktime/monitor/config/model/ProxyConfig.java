package com.restocktime.monitor.config.model;

import java.util.List;

public class ProxyConfig {
    private List<Proxies> proxiesList;

    public List<Proxies> getProxiesList() {
        return proxiesList;
    }

    public void setProxiesList(List<Proxies> proxiesList) {
        this.proxiesList = proxiesList;
    }
}
