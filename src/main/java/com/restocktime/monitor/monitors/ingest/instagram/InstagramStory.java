package com.restocktime.monitor.monitors.ingest.instagram;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.instagram.parse.InstagramStoryResponseParser;
import com.restocktime.monitor.monitors.ingest.shopify.Shopify;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

public class InstagramStory extends AbstractMonitor {

    private String url;
    private int delay;
    final static Logger logger = Logger.getLogger(Shopify.class);

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private InstagramStoryResponseParser instagramStoryResponseParser;
    private String sessionid;

    public InstagramStory(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, InstagramStoryResponseParser instagramStoryResponseParser, String sessionId){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.instagramStoryResponseParser = instagramStoryResponseParser;
        this.sessionid = sessionId;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);
        boolean found = false;
        for(Header h : basicRequestClient.getHeaderList()){
            if(h.getName().equals("Cookie")) {
                found = true;
            }
        }

        if(!found)
            basicRequestClient.getHeaderList().add(new BasicHeader("Cookie", sessionid));



        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);

            instagramStoryResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.info(e);

        }
    }

    public String getUrl(){
        return url;
    }
}
