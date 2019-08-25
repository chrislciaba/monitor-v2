package com.restocktime.monitor.monitors.parse.important.offwhite;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.http.request.wrapper.CloudflareRequestWrapper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.important.offwhite.parse.OffWhiteAllResponseParser;
import com.restocktime.monitor.monitors.parse.important.offwhite.parse.OffWhiteProductAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;

public class OffWhiteAll extends AbstractMonitor {

    private String monitorUrl;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private CloudflareRequestWrapper httpRequestHelper;
    private Notifications notifications;
    private OffWhiteProductAbstractResponseParser offWhiteProductResponseParser;
    private OffWhiteAllResponseParser offWhiteAllResponseParser;

    public OffWhiteAll(String monitorUrl, int delay,   AttachmentCreater attachmentCreater, AbstractHttpRequestHelper httpRequestHelper, OffWhiteAllResponseParser offWhiteAllResponseParser) {
        this.monitorUrl = monitorUrl;
        this.delay = delay;
        this.notifications = notifications;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = (CloudflareRequestWrapper)httpRequestHelper;
        this.offWhiteAllResponseParser = offWhiteAllResponseParser;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse response = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlRandNumberAppended(monitorUrl));
            offWhiteAllResponseParser.parse(response, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);

        } catch(Exception e){

        }
    }

    public String getUrl(){
        return monitorUrl;
    }
}

