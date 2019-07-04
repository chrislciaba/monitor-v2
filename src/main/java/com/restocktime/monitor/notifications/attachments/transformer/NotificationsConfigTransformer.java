package com.restocktime.monitor.notifications.attachments.transformer;

import com.restocktime.monitor.config.model.notifications.NotificationConfig;
import com.restocktime.monitor.config.model.notifications.DiscordObj;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.notifications.attachments.notification.NotificationBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsConfigTransformer {
    public static List<String> transformNotifications(NotificationConfig notificationConfig){
        Map<String, String> notifyMap = new HashMap<>();
        List<String> notifyList = new ArrayList<>();

        for(DiscordObj discordObj : notificationConfig.getDiscordList()){
            if(!notifyMap.containsKey(discordObj.getFormat())){
                notifyMap.put(discordObj.getFormat(), "");
                notifyList.add(discordObj.getFormat());
            }
        }

        for(SlackObj slackObj : notificationConfig.getSlackList()){
            if(!notifyMap.containsKey(slackObj.getFormat())){
                notifyMap.put(slackObj.getFormat(), "");
                notifyList.add(slackObj.getFormat());
            }
        }

        return notifyList;
    }
}
