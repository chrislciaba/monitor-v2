package com.restocktime.monitor.monitors.parse.supreme.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;

import java.util.ArrayList;
import java.util.List;

public class RestocktimeBuilder {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, String locale, String color, String price, List<String> sizes){
        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordFields = new ArrayList<>();
        //slackFields.add(new SlackField("Sizes", String.join("/", sizes), true));
       //discordFields.add(new DiscordField("Sizes", String.join("/", sizes), true));
        attachmentCreater.addMessages(url, null, name + " - " + color, "Supreme " + locale, slackFields, discordFields, imgUrl, null, "restocktime");
    }
}
