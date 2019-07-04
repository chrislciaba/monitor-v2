package com.restocktime.monitor.monitors.parse.walmart;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.walmart.parse.WalmartTerraResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public class WalmartTerra extends AbstractMonitor {

    private final String WALMART_ENDPOINT = "https://www.walmart.com/terra-firma/item/%s";
    private final String WALMART_PRODUCT_LINK = "https://www.walmart.com/ip/%s";

    private String url;
    private String producturl;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private WalmartTerraResponseParser walmartTerraResponseParser;

    public WalmartTerra(String sku, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, WalmartTerraResponseParser walmartTerraResponseParser){
        this.url = String.format(WALMART_ENDPOINT, sku);
        this.producturl = String.format(WALMART_PRODUCT_LINK, sku);
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.walmartTerraResponseParser = walmartTerraResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            BasicHttpResponse productHttpResponse = httpRequestHelper.performGet(basicRequestClient, producturl);
            walmartTerraResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){

        }

    }

    public String getUrl(){
        return url;
    }
}
