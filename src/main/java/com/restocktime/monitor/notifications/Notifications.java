package com.restocktime.monitor.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.parse.supreme.parse.SupremeAllProductResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.attachments.notification.DiscordNotification;
import com.restocktime.monitor.notifications.attachments.notification.Notification;
import com.restocktime.monitor.notifications.attachments.notification.SlackNotification;
import com.restocktime.monitor.notifications.client.Discord;
import com.restocktime.monitor.notifications.client.Slack;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Notifications {
    final static Logger logger = Logger.getLogger(Notifications.class);


    public static void send(AttachmentCreater attachmentCreater){
        Notification notification = attachmentCreater.getNotification();
        try {

            Map<String, String> found = new HashMap<>();

            for(String discordKey : notification.getDiscordNotification().keySet()){
                DiscordNotification discordNotification = notification.getDiscordNotification().get(discordKey);
                Discord discord = new Discord(discordNotification.getDiscordObj().getWebhook(), discordNotification.getEmbed(), null);
                discord.run();
            }

            for(SlackObj slackObj : attachmentCreater.getNotificationsConfig().getSlackList()){

                if(!found.containsKey(slackObj.getChannelId() + slackObj.getBotToken() +  slackObj.getFormat())) {
                    found.put(slackObj.getChannelId() + slackObj.getBotToken() + slackObj.getFormat(), "");
                    Slack slack = new Slack(
                            slackObj,
                            null,
                            new ArrayList<>(attachmentCreater.getMessages()));
                    slack.run();
                }
            }

            for(String slackKey : notification.getSlackNotification().keySet()){
                SlackNotification slackNotification = notification.getSlackNotification().get(slackKey);

                Slack slack = new Slack(
                        slackNotification.getSlackObj(),
                        slackNotification.getAttachment(),
                        new ArrayList<>());
                slack.run();
            }
        } catch(Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }
}
