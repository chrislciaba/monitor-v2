package com.restocktime.monitor.monitors.parse.snkrs.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class SnkrsBuilder {
    public static void buildAttachments(AttachmentCreater attachmentCreater, String url, String imgUrl, String rtUrl, String name, String launchType, String selectionEngine, String releaseDate, String launchDateStr, String sku, String region, List<String> keys) {
        for(String format : keys){
            if(format.equals("restocktime")){
                RestocktimeBuilder.addAttachment(attachmentCreater, url, imgUrl,rtUrl, name, launchType, selectionEngine, releaseDate, launchDateStr, sku, region);
            }
        }
    }
}
