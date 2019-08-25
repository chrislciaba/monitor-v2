package com.restocktime.monitor.monitors.ingest.aio.lvr;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.aio.lvr.parse.LvrResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

public class LVR extends AbstractMonitor {
    final static Logger logger = Logger.getLogger(LVR.class);

    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private LvrResponseParser lvrResponseParser;

    public LVR(String url, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, LvrResponseParser lvrResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.lvrResponseParser = lvrResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse response = httpRequestHelper.performGet(basicRequestClient, url);
            lvrResponseParser.parse(response, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){

        }
    }

    public String getUrl(){
        return url;
    }
}