package com.restocktime.monitor.monitors.ingest.twitter;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.shopify.Shopify;
import com.restocktime.monitor.monitors.parse.twitter.parse.TwitterAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Twitter extends AbstractMonitor {

    private String url;
    private int delay;
    final static Logger log = Logger.getLogger(Shopify.class);
    private Pattern noScriptLinkPattern = Pattern.compile("<form action=\"([^\"]*)\" method=\"POST\"");

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private TwitterAbstractResponseParser twitterResponseParser;

    public Twitter(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, TwitterAbstractResponseParser twitterResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.twitterResponseParser = twitterResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            if(basicHttpResponse.getBody().get().contains("nojs_router")){
                Matcher m = noScriptLinkPattern.matcher(basicHttpResponse.getBody().get());
                if(m.find()){
                    basicHttpResponse = httpRequestHelper.performPost(basicRequestClient, m.group(1), "");

                }
            }
            twitterResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
