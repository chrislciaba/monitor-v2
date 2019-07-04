package com.restocktime.monitor.notifications.attachments.notification;

import java.util.Map;

public class Notification {
    private Map<String, SlackNotification> slackNotification; //key = botToken + channelId
    private Map<String, DiscordNotification> discordNotification;

    public Notification(Map<String, SlackNotification> slackNotification, Map<String, DiscordNotification> discordNotification) {
        this.slackNotification = slackNotification;
        this.discordNotification = discordNotification;
    }

    public Map<String, SlackNotification> getSlackNotification() {
        return slackNotification;
    }

    public void setSlackNotification(Map<String, SlackNotification> slackNotification) {
        this.slackNotification = slackNotification;
    }

    public Map<String, DiscordNotification> getDiscordNotification() {
        return discordNotification;
    }

    public void setDiscordNotification(Map<String, DiscordNotification> discordNotification) {
        this.discordNotification = discordNotification;
    }
}
