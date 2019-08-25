package com.restocktime.monitor.monitors.parse.social.instagram.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class DefaultInstagramStory {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String name, String imgUrl, String format){

        attachmentCreater.addMessages(url, null, name, "IG STORY", null, null, imgUrl, null, format);
    }
}
