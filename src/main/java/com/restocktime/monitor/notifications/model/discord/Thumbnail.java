package com.restocktime.monitor.notifications.model.discord;

public class Thumbnail {
    private String url;

    public Thumbnail(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
