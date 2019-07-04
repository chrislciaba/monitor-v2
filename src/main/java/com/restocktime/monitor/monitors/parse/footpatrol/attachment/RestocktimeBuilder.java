package com.restocktime.monitor.monitors.parse.footpatrol.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class RestocktimeBuilder {
    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, String site){
        url = "https://www.footpatrol.com/product/restocktime/" + url;
        attachmentCreater.addMessages(url,null, name, site, null, null, imgUrl, null, "restocktime");
    }
}
