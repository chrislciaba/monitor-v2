package com.restocktime.monitor.notifications.attachments;

import com.restocktime.monitor.config.model.FormatConfig;
import com.restocktime.monitor.config.model.NotificationsFormatConfig;
import com.restocktime.monitor.config.model.notifications.DiscordObj;
import com.restocktime.monitor.config.model.notifications.NotificationConfig;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.notifications.attachments.notification.DiscordNotification;
import com.restocktime.monitor.notifications.attachments.notification.Notification;
import com.restocktime.monitor.notifications.attachments.notification.SlackNotification;
import com.restocktime.monitor.notifications.model.discord.*;
import com.restocktime.monitor.notifications.model.slack.Attachment;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.notifications.client.Slack;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttachmentCreater {
    final static Logger logger = Logger.getLogger(AttachmentCreater.class);

    private List<String> messages;
    private NotificationsFormatConfig notificationsFormatConfig;
    private NotificationConfig notificationsConfig;
    private Notification notification;

    public AttachmentCreater(NotificationConfig notificationConfig, NotificationsFormatConfig notificationsFormatConfig){
        messages = new ArrayList<>();
        this.notificationsFormatConfig = notificationsFormatConfig;
        this.notificationsConfig = notificationConfig;
        this.notification = new Notification(new HashMap<>(), new HashMap<>());
    }

    public AttachmentCreater(AttachmentCreater attachmentCreater){
        this.messages = new ArrayList<>();
        this.notificationsFormatConfig = attachmentCreater.notificationsFormatConfig;
        this.notificationsConfig = attachmentCreater.notificationsConfig;
        this.notification = new Notification(new HashMap<>(), new HashMap<>());
    }

    public void addTextMessage(String message){
        messages.add(message);
    }

    public void addMessages(String url, String author_name, String name, String site, List<SlackField> slackFields, List<DiscordField> discordDiscordFields, String thumbUrl, String imgUrl, String key){
        createSlackAttachment(url, name, author_name, site, slackFields, thumbUrl, imgUrl, key);
        addEmbed(url, name, site, discordDiscordFields, thumbUrl, imgUrl, author_name, key);
    }

    public void createSlackAttachment(String url, String name, String author_name, String site, List<SlackField> slackFields, String thumbUrl, String imgUrl, String key){
        String altText = "%s";
        if(slackFields == null){
            slackFields = new ArrayList<>();
        }


        for(SlackObj slackObj : notificationsConfig.getSlackList()) {
            logger.info(slackObj.getFormat());
            if(slackObj.getFormat().equals(key)) {
                FormatConfig formatConfig = notificationsFormatConfig.getSlack().get(slackObj.getFormat());
                Attachment attachment = new Attachment(
                        site,
                        author_name,
                        name,
                        formatConfig.getColor(),
                        name,
                        url,
                        imgUrl,
                        thumbUrl,
                        formatConfig.getFooter(),
                        formatConfig.getFooterUrl(),
                        Long.toString(System.currentTimeMillis() / 1000L),
                        slackFields
                );

                String findKey = slackObj.getBotToken() + slackObj.getChannelId() + slackObj.getFormat();

                if(!notification.getSlackNotification().containsKey(findKey)){
                    List<Attachment> newList = new ArrayList<>();
                    newList.add(attachment);
                    SlackNotification slackNotification = new SlackNotification(slackObj, newList);
                    notification.getSlackNotification().put(findKey, slackNotification);
                } else {
                    notification.getSlackNotification().get(findKey).getAttachment().add(attachment);

                }
            }


        }
    }

    public void addCallback(){

    }

    public void addEmbed(String url, String name, String site, List<DiscordField> discordFields, String thumbUrl, String imgUrl, String author, String key){

        for(DiscordObj discordObj : notificationsConfig.getDiscordList()) {
            if(discordObj.getFormat().equals(key)) {
                FormatConfig formatConfig = notificationsFormatConfig.getDiscord().get(key);

                Footer footer = new Footer(formatConfig.getFooter(), formatConfig.getFooterUrl());
                Thumbnail thumbnail = new Thumbnail(thumbUrl);
                Embed embed = new Embed(name, formatConfig.getColor(), url, footer, site, discordFields,thumbnail, new Author(author), imgUrl != null ? Image.builder().url(imgUrl).build() : null);
                String findKey = discordObj.getWebhook() + discordObj.getFormat();

                if(!notification.getDiscordNotification().containsKey(findKey)){
                    List<Embed> newList = new ArrayList<>();
                    newList.add(embed);
                    DiscordNotification discordNotification = new DiscordNotification(discordObj, newList);
                    notification.getDiscordNotification().put(findKey, discordNotification);
                } else {
                    notification.getDiscordNotification().get(findKey).getEmbed().add(embed);

                }
            }
        }
    }

    public void addCompletedAttachment(Attachment attachment, String key){
        for(SlackObj slackObj : notificationsConfig.getSlackList()) {
            if(slackObj.getFormat().equals(key)) {
                String findKey = slackObj.getBotToken() + slackObj.getChannelId() + slackObj.getFormat();

                if(!notification.getSlackNotification().containsKey(findKey)){
                    List<Attachment> newList = new ArrayList<>();
                    newList.add(attachment);
                    SlackNotification slackNotification = new SlackNotification(slackObj, newList);
                    notification.getSlackNotification().put(findKey, slackNotification);
                } else {
                    notification.getSlackNotification().get(findKey).getAttachment().add(attachment);
                }
            }
        }
    }

    public boolean isEmpty(){
        return notification.getDiscordNotification().isEmpty() && notification.getSlackNotification().isEmpty();
    }

    public Notification getNotification() {
        return notification;
    }

    public void clearAll(){
        notification.getDiscordNotification().clear();
        notification.getSlackNotification().clear();
        messages.clear();
    }

    public NotificationConfig getNotificationsConfig() {
        return notificationsConfig;
    }

    public List<String> getMessages() {
        return messages;
    }
}
