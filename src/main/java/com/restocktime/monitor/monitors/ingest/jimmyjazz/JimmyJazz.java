package com.restocktime.monitor.monitors.ingest.jimmyjazz;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.jimmyjazz.parse.JimmyJazzResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

public class JimmyJazz extends AbstractMonitor {
    private String url;
    private int delay;
    final static Logger logger = Logger.getLogger(JimmyJazz.class);

    private AttachmentCreater attachmentCreater;
    private CloudflareRequestHelper cloudflareRequestHelper;
    private JimmyJazzResponseParser jimmyJazzResponseParser;

    public JimmyJazz(String url, int delay,   AttachmentCreater attachmentCreater, CloudflareRequestHelper cloudflareRequestHelper, JimmyJazzResponseParser jimmyJazzResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.cloudflareRequestHelper = cloudflareRequestHelper;
        this.jimmyJazzResponseParser = jimmyJazzResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {

            if(url.contains("search.jimmyjazz")) {
                basicRequestClient.getCookieStore().clear();
                cloudflareRequestHelper.performGet(basicRequestClient, "http://www.jimmyjazz.com/mens/footwear/nike-react-element-55/BQ6166-006?color=Black");
                Timeout.timeout(delay);
            }
            BasicHttpResponse basicHttpResponse = cloudflareRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));


            jimmyJazzResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);




        } catch(Exception e){
            logger.info(e);

        }
    }

    public String getUrl(){
        return url;
    }
}
