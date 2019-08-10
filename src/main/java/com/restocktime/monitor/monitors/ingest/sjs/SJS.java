package com.restocktime.monitor.monitors.ingest.sjs;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class SJS extends AbstractMonitor {
    //<a class="product_img_link" href="https://www.slamjamsocialism.com/high-top/53530-air-jordan-1-retro-high-og-sneakers.html" title="Air Jordan 1 Retro High OG Sneakers"

    final static Logger log = Logger.getLogger(SJS.class);

    private String url;
    private int delay;

    private String[] discordWebhook;
    private SlackObj[] slackObj;
    Map<String, Integer> alreadyFound;

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;

    public SJS(String url, int delay,  SlackObj[] slackObj, String[] discordWebhook, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper){
        this.url = url;
        this.delay = delay;
        this.slackObj = slackObj;
        this.discordWebhook = discordWebhook;

        alreadyFound = new HashMap<>();
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();



        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //notify slack
        }



        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);

           // if(attachmentCreater.getSlackAttachments().size() > 0) {
              //  Notifications.send(discordWebhook, slackObj, attachmentCreater);
        //    } else {
        //        logger.info("No products found");
        //    }

        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }

}
