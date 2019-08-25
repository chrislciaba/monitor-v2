package com.restocktime.monitor.monitors.ingest.aio.ccs;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.aio.parse.CCSResponseParser;
import com.restocktime.monitor.monitors.ingest.important.shopify.Shopify;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

public class CCS extends AbstractMonitor {
    private String url;
    private int delay;
    final static Logger logger = Logger.getLogger(Shopify.class);

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private CCSResponseParser ccsResponseParser;

    public CCS(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, CCSResponseParser ccsResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.ccsResponseParser = ccsResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            ccsResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.info(e);

        }
    }

    public String getUrl(){
        return url;
    }
}