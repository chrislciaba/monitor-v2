package com.restocktime.monitor.monitors.ingest.disney;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.disney.parse.DisneyRestockResponseParser;
import com.restocktime.monitor.monitors.ingest.shopify.Shopify;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

public class DisneyRestock extends AbstractMonitor {
    private String url;
    private int delay;
    final static Logger logger = Logger.getLogger(Shopify.class);

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private DisneyRestockResponseParser disneyRestockResponseParser;

    public DisneyRestock(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper,DisneyRestockResponseParser disneyRestockResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.disneyRestockResponseParser = disneyRestockResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));
            disneyRestockResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.info(e);

        }
    }

    public String getUrl(){
        return url;
    }
}
