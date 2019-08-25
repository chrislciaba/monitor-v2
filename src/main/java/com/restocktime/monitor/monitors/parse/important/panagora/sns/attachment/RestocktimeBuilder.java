package com.restocktime.monitor.monitors.parse.important.panagora.sns.attachment;

import com.restocktime.monitor.monitors.parse.important.panagora.sns.model.SizeObj;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.model.discord.DiscordField;

import java.util.ArrayList;
import java.util.List;

public class RestocktimeBuilder {
    private static final String DISCORD_FORMAT = "[ATC](%s)";
    private static final String ATC_LINK_FORMAT = "https://www.sneakersnstuff.com/en/cart/add?partial=ajax-cart&id=%s";

    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, List<SizeObj> sizeObjs, String key) {
        List<DiscordField> discordFields = new ArrayList<>();

        for (SizeObj sizeObj : sizeObjs) {
            discordFields.add(
                    new DiscordField(
                            sizeObj.getSize(),
                            String.format(
                                    DISCORD_FORMAT,
                                    String.format(
                                            ATC_LINK_FORMAT,
                                            sizeObj.getSku())
                            ),
                            true
                    )
            );
        }

        attachmentCreater.addMessages(url, "", name, "SNS", null, discordFields, imgUrl, null, key);

    }
}
