package com.restocktime.monitor.monitors.parse.snkrs.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class RestocktimeHuntBuilder {
    public static void addAttachment(AttachmentCreater attachmentCreater, String type, boolean isScratch, String topImage, String baseImage, String region){

        if(isScratch){
            attachmentCreater.addMessages(topImage, null, "TOP IMAGE", "Snkrs Hunt " + region, null, null, null, topImage, "restocktime");
            attachmentCreater.addMessages(topImage, null, "BASE IMAGE", "Snkrs Hunt " + region, null, null, null, baseImage, "restocktime");
        } else {
            attachmentCreater.addMessages("", null, "HUNT LOAD: " + type, "Snkrs Hunt " + region, null, null, null, null, "restocktime");
        }
    }
}
