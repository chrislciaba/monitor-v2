package com.restocktime.monitor.config.model.notifications;

public class DiscordObj {
    private String webhook;
    private String format;

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
