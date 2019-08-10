package com.restocktime.monitor.monitors.parse.offwhite;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteSoldOutTagResponseParser;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;

public class OffWhitePage extends AbstractMonitor {

    private String monitorUrl;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private CloudflareRequestHelper httpRequestHelper;
    private OffWhiteSoldOutTagResponseParser offWhiteSoldOutTagResponseParser;

    public OffWhitePage(String monitorUrl, int delay, AttachmentCreater attachmentCreater, AbstractHttpRequestHelper httpRequestHelper, OffWhiteSoldOutTagResponseParser offWhiteSoldOutTagResponseParser) {
        this.monitorUrl = monitorUrl;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = (CloudflareRequestHelper)httpRequestHelper;
        this.offWhiteSoldOutTagResponseParser = offWhiteSoldOutTagResponseParser;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse response = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlRandNumberAppended(monitorUrl));
            offWhiteSoldOutTagResponseParser.parse(response, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){

        }
    }

    public String getUrl(){
        return monitorUrl;
    }
}
