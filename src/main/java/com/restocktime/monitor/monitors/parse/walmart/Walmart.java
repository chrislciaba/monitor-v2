package com.restocktime.monitor.monitors.parse.walmart;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.walmart.parse.WalmartResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class Walmart extends AbstractMonitor {

    private final String WALMART_ENDPOINT = "https://www.walmart.com/preso/search?query=%s";

    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private WalmartResponseParser walmartResponseParser;

    public Walmart(String sku, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, WalmartResponseParser walmartResponseParser){
        this.url = String.format(WALMART_ENDPOINT, sku);
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.walmartResponseParser = walmartResponseParser;
    }


    public  void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            walmartResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){

        }

    }

    public String getUrl(){
        return url;
    }
}
