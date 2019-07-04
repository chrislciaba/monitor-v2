package com.restocktime.monitor.monitors.parse.instagram.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class DefaultInstagram {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String text, String person, String imgUrl, String format){

        attachmentCreater.addMessages(url, null, person, text, null, null, null, imgUrl, format);
    }
}
