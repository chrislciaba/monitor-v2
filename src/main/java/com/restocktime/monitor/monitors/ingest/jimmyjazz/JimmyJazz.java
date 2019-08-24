package com.restocktime.monitor.monitors.ingest.jimmyjazz;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.wrapper.CloudflareRequestWrapper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.jimmyjazz.parse.JimmyJazzResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class JimmyJazz extends AbstractMonitor {
    private String url;
    private int delay;
    final static Logger log = Logger.getLogger(JimmyJazz.class);

    private AttachmentCreater attachmentCreater;
    private CloudflareRequestWrapper cloudflareRequestWrapper;
    private JimmyJazzResponseParser jimmyJazzResponseParser;

    public JimmyJazz(String url, int delay, AttachmentCreater attachmentCreater, CloudflareRequestWrapper cloudflareRequestWrapper, JimmyJazzResponseParser jimmyJazzResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.cloudflareRequestWrapper = cloudflareRequestWrapper;
        this.jimmyJazzResponseParser = jimmyJazzResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {

            if(url.contains("search.jimmyjazz")) {
                basicRequestClient.getCookieStore().clear();
                cloudflareRequestWrapper.performGet(basicRequestClient, "http://www.jimmyjazz.com/mens/footwear/nike-react-element-55/BQ6166-006?color=Black");
                Timeout.timeout(delay);
            }
            BasicHttpResponse basicHttpResponse = cloudflareRequestWrapper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));


            jimmyJazzResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);




        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
