package com.restocktime.monitor.monitors.parse.twitter.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class DefaultTwitter {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String text, String person, String format){

        attachmentCreater.addMessages(url, null, person + " on twitter", text, null, null, null, null, format);
    }
}
