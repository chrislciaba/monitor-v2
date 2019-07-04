package com.restocktime.monitor.helper.anticap.model;

public class Task {
    public String type;
    public String websiteURL;
    public String websiteKey;
    public String proxyType;
    public String proxyLigin;
    public String proxyPassword;
    public String userAgent;

    public Task(String type, String websiteURL, String websiteKey, String proxyType, String proxyLigin, String proxyPassword, String userAgent) {
        this.type = type;
        this.websiteURL = websiteURL;
        this.websiteKey = websiteKey;
        this.proxyType = proxyType;
        this.proxyLigin = proxyLigin;
        this.proxyPassword = proxyPassword;
        this.userAgent = userAgent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getWebsiteKey() {
        return websiteKey;
    }

    public void setWebsiteKey(String websiteKey) {
        this.websiteKey = websiteKey;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyLigin() {
        return proxyLigin;
    }

    public void setProxyLigin(String proxyLigin) {
        this.proxyLigin = proxyLigin;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
