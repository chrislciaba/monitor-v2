package com.restocktime.monitor.notifications.defaultattachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class RestocktimeBuilder {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, String site){
        attachmentCreater.addMessages(url,null, name, site, null, null, imgUrl, null, "restocktime");
    }
}
