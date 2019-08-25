package com.restocktime.monitor.monitors.ingest.social.instagram;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.social.instagram.parse.InstagramStoryResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class InstagramStory extends AbstractMonitor {

    private String url;
    private int delay;
    final static Logger log = Logger.getLogger(InstagramStory.class);

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
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
