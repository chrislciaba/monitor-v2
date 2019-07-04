package com.restocktime.monitor.notifications.attachments.notification;

import java.util.List;

public class NotificationBuilder {
    private List<String> discord;
    private List<String> slack;

    public List<String> getDiscord() {
        return discord;
    }

    public void setDiscord(List<String> discord) {
        this.discord = discord;
    }

    public List<String> getSlack() {
        return slack;
    }

    public void setSlack(List<String> slack) {
        this.slack = slack;
    }
}
