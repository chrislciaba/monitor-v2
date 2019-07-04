package com.restocktime.monitor.config.model.notifications;

import java.util.ArrayList;
import java.util.List;

public class NotificationConfig {
    private List<DiscordObj> discordList;
    private List<SlackObj> slackList;

    public NotificationConfig(){
        discordList = new ArrayList<>();
        slackList = new ArrayList<>();
    }

    public List<DiscordObj> getDiscordList() {
        return discordList;
    }

    public void setDiscordList(List<DiscordObj> discordList) {
        this.discordList = discordList;
    }

    public List<SlackObj> getSlackList() {
        return slackList;
    }

    public void setSlackList(List<SlackObj> slackList) {
        this.slackList = slackList;
    }
}
