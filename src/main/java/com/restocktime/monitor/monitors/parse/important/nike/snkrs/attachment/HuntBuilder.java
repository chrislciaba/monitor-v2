package com.restocktime.monitor.monitors.parse.important.nike.snkrs.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class HuntBuilder {
    public static void buildAttachments(AttachmentCreater attachmentCreater, String type, boolean isScratch, String topImage, String baseImage, String region, List<String> keys) {
        for(String format : keys){
            if(format.equals("restocktime")){
                RestocktimeHuntBuilder.addAttachment(attachmentCreater, type, isScratch, topImage, baseImage, region);
            }
        }
    }
}
