package com.restocktime.monitor.monitors.parse.important.offwhite;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.http.request.wrapper.CloudflareRequestWrapper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.important.offwhite.parse.OffWhiteSoldOutTagResponseParser;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;

public class OffWhitePage extends AbstractMonitor {

    private String monitorUrl;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private CloudflareRequestWrapper httpRequestHelper;
    private OffWhiteSoldOutTagResponseParser offWhiteSoldOutTagResponseParser;

    public OffWhitePage(String monitorUrl, int delay, AttachmentCreater attachmentCreater, AbstractHttpRequestHelper httpRequestHelper, OffWhiteSoldOutTagResponseParser offWhiteSoldOutTagResponseParser) {
        this.monitorUrl = monitorUrl;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = (CloudflareRequestWrapper)httpRequestHelper;
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
