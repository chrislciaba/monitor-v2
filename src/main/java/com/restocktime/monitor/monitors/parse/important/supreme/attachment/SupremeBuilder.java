package com.restocktime.monitor.monitors.parse.important.supreme.attachment;

import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class SupremeBuilder {

    public static void buildAttachments(AttachmentCreater attachmentCreater, String url, String imgUrl, String locale, String name, String color, String price, List<String> sizes, List<String> keys) {
        for(String format : keys){
            if(format.equals("restocktime")){
                RestocktimeBuilder.addAttachment(attachmentCreater, url, imgUrl, name, locale, color, price, sizes);
            }
        }
    }
}
