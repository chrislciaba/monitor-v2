package com.restocktime.monitor.monitors.parse.offwhite.model.offwhite;

public class OffWhiteThreadObj {
    private String url;
    private String tag;

    public OffWhiteThreadObj(String tag, String url) {
        this.url = url;
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
