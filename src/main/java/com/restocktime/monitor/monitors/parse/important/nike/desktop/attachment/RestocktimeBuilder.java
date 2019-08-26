package com.restocktime.monitor.monitors.parse.important.nike.desktop.attachment;

import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.SizeAndStock;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;

import java.util.ArrayList;
import java.util.List;

public class RestocktimeBuilder {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String rtUrl, String name, List<SizeAndStock> sizeAndStocks, String sku){
        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordFields = new ArrayList<>();
        discordFields.add(new DiscordField("Style Code", formatField(sku), false));
        for (SizeAndStock sizeAndStock : sizeAndStocks) {
            discordFields.add(new DiscordField(sizeAndStock.getSize(), sizeAndStock.getStock(), true));
        }

        attachmentCreater.addMessages(url, null, name, "Nike Desktop", slackFields, discordFields, rtUrl, null, "restocktime");
    }

    private static String formatField(String field){
        if(field == null){
            return "N/A";
        }

        return field;
    }
}
