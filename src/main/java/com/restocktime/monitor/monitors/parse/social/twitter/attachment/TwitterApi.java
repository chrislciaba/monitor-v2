package com.restocktime.monitor.monitors.parse.social.twitter.attachment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.monitors.parse.social.twitter.parse.model.User;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.model.discord.DiscordField;

import java.util.*;
import java.util.stream.Collectors;

public class TwitterApi {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String text, String mainUser, String format, List<String> images, List<User> users){
        List<DiscordField> discordFieldList = new ArrayList<>();
        discordFieldList.add(new DiscordField("Main User", mainUser, false));
        discordFieldList.add(new DiscordField("Text Content", text, false));
        attachmentCreater.addMessages(url, null, mainUser + " on twitter", text, null, discordFieldList, null, null, format);
        for (int i = 0; i < images.size(); i++) {
            attachmentCreater.addMessages(images.get(i), null, "Image " + (i + 1), null, null, null, null, images.get(i), format);
        }

        Set<String> keys = new HashSet<>();

        for (User user : users) {
            if(keys.contains(user.getScreen_name()))
                continue;
            keys.add(user.getScreen_name());

            List<DiscordField> userDisc = new ArrayList<>();
            userDisc.add(new DiscordField("Bio", user.getDescription(), false));
            userDisc.add(new DiscordField("Location", user.getLocation(), false));
            userDisc.add(new DiscordField("Name", user.getName(), false));

            attachmentCreater.addMessages("https://twitter.com/" + user.getScreen_name(), "Twitter User", user.getName(),null, null, userDisc, user.getProfile_image_url_https(), user.getProfile_banner_url(), format);
        }
    }
}
