package com.restocktime.monitor.notifications.defaultattachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class DefaultBuilder {

    public static void buildAttachments(AttachmentCreater attachmentCreater, String url, String imgUrl, String site, String name, List<String> keys) {
        for(String format : keys){
            if(format.equals("restocktime")){
                RestocktimeBuilder.addAttachment(attachmentCreater, url, imgUrl, name, site);
            } else if(format.equals("solefood")){
                SolefoodBuilder.addAttachment(attachmentCreater, url, imgUrl, name, site);
            } else if(format.equals("endurance")){
                EnduranceBuilder.addAttachment(attachmentCreater, url, imgUrl, name, site);
            }
        }
    }
}
