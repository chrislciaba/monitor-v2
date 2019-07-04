package com.restocktime.monitor.proxymanager.model;

import java.util.List;

public class ProxyList {
    List<String> proxies;
    Integer listSize;

    public List<String> getProxies() {
        return proxies;
    }

    public void setProxies(List<String> proxies) {
        this.proxies = proxies;
    }

    public Integer getListSize() {
        return listSize;
    }

    public void setListSize(Integer listSize) {
        this.listSize = listSize;
    }
}
