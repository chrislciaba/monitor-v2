package com.restocktime.monitor.monitors.parse.mesh.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class FootpatrolBuilder {

    public static void buildAttachments(AttachmentCreater attachmentCreater, String url, String imgUrl, String site, String name, List<String> keys) {
        for(String format : keys){
            if(format.equals("restocktime")){
                RestocktimeBuilder.addAttachment(attachmentCreater, url, imgUrl, name, site);
            }
        }
    }
}
