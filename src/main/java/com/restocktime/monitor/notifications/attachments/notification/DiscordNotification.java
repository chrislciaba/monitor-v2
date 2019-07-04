package com.restocktime.monitor.notifications.attachments.notification;

import com.restocktime.monitor.config.model.notifications.DiscordObj;
import com.restocktime.monitor.notifications.model.discord.Embed;

import java.util.List;

public class DiscordNotification {
    private DiscordObj discordObj;
    private List<Embed> embed;

    public DiscordNotification(DiscordObj discordObj, List<Embed> embed) {
        this.discordObj = discordObj;
        this.embed = embed;
    }

    public DiscordObj getDiscordObj() {
        return discordObj;
    }

    public void setDiscordObj(DiscordObj discordObj) {
        this.discordObj = discordObj;
    }

    public List<Embed> getEmbed() {
        return embed;
    }

    public void setEmbed(List<Embed> embed) {
        this.embed = embed;
    }
}
