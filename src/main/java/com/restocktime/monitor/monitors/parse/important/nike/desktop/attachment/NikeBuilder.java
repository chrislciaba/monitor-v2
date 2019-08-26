package com.restocktime.monitor.monitors.parse.important.nike.desktop.attachment;

import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.SizeAndStock;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class NikeBuilder {
    public static void buildAttachments(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, List<SizeAndStock> sizeAndStocks, String sku, List<String> keys) {
        for(String format : keys){
            if(format.equals("restocktime")){
                RestocktimeBuilder.addAttachment(attachmentCreater, url, imgUrl, name, sizeAndStocks, sku);
            }
        }
    }
}
