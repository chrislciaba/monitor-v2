package com.restocktime.monitor.config.model;

import java.util.Map;

public class NotificationsFormatConfig {
    private Map<String, FormatConfig> slack;
    private Map<String, FormatConfig> discord;

    public Map<String, FormatConfig> getSlack() {
        return slack;
    }

    public void setSlack(Map<String, FormatConfig> slack) {
        this.slack = slack;
    }

    public Map<String, FormatConfig> getDiscord() {
        return discord;
    }

    public void setDiscord(Map<String, FormatConfig> discord) {
        this.discord = discord;
    }
}
