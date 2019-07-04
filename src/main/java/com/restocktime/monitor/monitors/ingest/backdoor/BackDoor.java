package com.restocktime.monitor.monitors.ingest.backdoor;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.backdoor.parse.BackdoorSearchAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class BackDoor extends AbstractMonitor {

    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private BackdoorSearchAbstractResponseParser backdoorSearchResponseParser;

    public BackDoor(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, BackdoorSearchAbstractResponseParser backdoorSearchResponseParser) {
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.backdoorSearchResponseParser = backdoorSearchResponseParser;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            String cookie = backdoorSearchResponseParser.getAndSetCookie(basicHttpResponse);

            if(cookie != null){
                for(Header h : basicRequestClient.getHeaderList()){
                    BasicHeader b = (BasicHeader)h;
                    if(b.getName().equals("Cookie")) {
                        basicRequestClient.getHeaderList().remove(h);
                        break;
                    }
                }
                basicRequestClient.getHeaderList().add(new BasicHeader("Cookie", cookie));
                basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            }
            backdoorSearchResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);


        } catch(Exception e){

        }
    }

    public String getUrl(){return "";}
}
