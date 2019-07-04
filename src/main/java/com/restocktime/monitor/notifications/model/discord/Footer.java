package com.restocktime.monitor.notifications.model.discord;

public class Footer {
    private String text;
    private String icon_url;

    public Footer(String text, String icon_url) {
        this.text = text.replaceAll("[^\\x00-\\x7F]", "");
        this.icon_url = icon_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
