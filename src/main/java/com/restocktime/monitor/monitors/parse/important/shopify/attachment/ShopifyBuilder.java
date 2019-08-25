package com.restocktime.monitor.monitors.parse.important.shopify.attachment;

import com.restocktime.monitor.monitors.parse.important.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;
import java.util.Optional;

public class ShopifyBuilder {

    public static void buildAttachments(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, String price, List<Variant> variants, String keywords, List<String> keys, int num, Optional<String> dsmKey, Optional<String> dsmValue) {

        for(String format : keys){
            if(format.equals("restocktime") || format.equals("restocktime-unfiltered")){
                RestocktimeBuilder.addAttachment(attachmentCreater, url, imgUrl, name, price, variants, num, format, dsmKey, dsmValue);
            }else if(format.equals("solefood") || format.equals("solefood-unfiltered")){
                SolefoodBuilder.addAttachment(attachmentCreater, url, imgUrl, name, price, variants, num, format);
            }
        }
    }
}
