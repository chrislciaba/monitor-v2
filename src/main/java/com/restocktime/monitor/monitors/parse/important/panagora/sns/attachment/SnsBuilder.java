package com.restocktime.monitor.monitors.parse.important.panagora.sns.attachment;

import com.restocktime.monitor.monitors.parse.important.panagora.sns.model.SizeObj;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class SnsBuilder {
    public static void buildAttachments(AttachmentCreater attachmentCreater, String url, String name, String imgUrl, List<SizeObj> sizes, List<String> keys) {
        for(String format : keys){
            if(format.equals("restocktime") || format.equals("endurance")){
                RestocktimeBuilder.addAttachment(attachmentCreater, url, imgUrl, name, sizes, format);
            }
        }
    }
}
