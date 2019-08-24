package com.restocktime.monitor.monitors.parse.offwhite;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.httprequests.wrapper.CloudflareRequestWrapper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteAllResponseParser;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteProductAbstractResponseParser;
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

