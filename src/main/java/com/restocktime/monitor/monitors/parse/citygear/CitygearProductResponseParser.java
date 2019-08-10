package com.restocktime.monitor.monitors.parse.citygear;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CitygearProductResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    Pattern pattern = Pattern.compile("<title>(.*)</title>");
    private String url;
    private List<String> formatNames;


    public CitygearProductResponseParser(String url, StockTracker stockTracker, List<String> formatNames){
        this.url = url;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String s = basicHttpResponse.getBody().get();

        Matcher m = pattern.matcher(s);
        if(s.contains("productAddToCartForm.submit(this)")){
            String productName = "NAME_UNAVAILABLE";
            if(m.find()){
                productName = m.group(1);
            }


            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, null,"Citygear", productName, formatNames);
            } else {
                stockTracker.setOOS(url);
            }
        }
    }
}
