package com.restocktime.monitor.monitors.parse.important.snkrs.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;

import java.util.ArrayList;
import java.util.List;

public class RestocktimeBuilder {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String imgUrl, String rtUrl, String name, String launchType, String selectionEngine, String releaseDate, String launchDateStr, String sku, String region){
        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordFields = new ArrayList<>();
        slackFields.add(new SlackField("Launch Date", formatField(releaseDate)));
        slackFields.add(new SlackField("Launch Type", formatField(launchType)));
        slackFields.add(new SlackField("Selection Engine", formatField(selectionEngine)));
        slackFields.add(new SlackField("Style Code", formatField(sku)));

        discordFields.add(new DiscordField("Launch Date", formatField(launchDateStr), false));
        discordFields.add(new DiscordField("Launch Type", formatField(launchType), false));
        discordFields.add(new DiscordField("Selection Engine", formatField(selectionEngine), false));
        discordFields.add(new DiscordField("Style Code", formatField(sku), false));

        attachmentCreater.addMessages(url, null, name, "SNKRS " + region, slackFields, discordFields, rtUrl, null, "restocktime");
    }

    private static String formatField(String field){
        if(field == null){
            return "N/A";
        }

        return field;
    }
}
