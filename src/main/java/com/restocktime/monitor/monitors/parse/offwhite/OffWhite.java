package com.restocktime.monitor.monitors.parse.offwhite;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteProductAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteSearchAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;

import java.util.Random;


public class OffWhite extends AbstractMonitor {

    private String monitorUrl;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private CloudflareRequestHelper httpRequestHelper;
    private Notifications notifications;
    private OffWhiteProductAbstractResponseParser offWhiteProductResponseParser;
    private OffWhiteSearchAbstractResponseParser offWhiteSearchResponseParser;
    private String locale;
    private final String URL_TEMPLATE = "%s.json?country=%s&locale=%s";

    public OffWhite(String monitorUrl, String locale, int delay, AttachmentCreater attachmentCreater, AbstractHttpRequestHelper httpRequestHelper, OffWhiteProductAbstractResponseParser offWhiteProductResponseParser, OffWhiteSearchAbstractResponseParser offWhiteSearchResponseParser) {
        this.monitorUrl = monitorUrl;
        this.delay = delay;
        this.notifications = notifications;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = (CloudflareRequestHelper)httpRequestHelper;
        this.offWhiteProductResponseParser = offWhiteProductResponseParser;
        this.offWhiteSearchResponseParser = offWhiteSearchResponseParser;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse response = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlRandNumberAppended(monitorUrl));
            if(monitorUrl.contains("search?")) {
                offWhiteSearchResponseParser.parse(response, attachmentCreater, isFirst);
            } else {
                offWhiteProductResponseParser.parse(response, attachmentCreater, isFirst);
            }
            Notifications.send(attachmentCreater);

        } catch(Exception e){

        }
    }

    private String genOffWhiteUrl(String url){
        int rand = Math.abs(new Random().nextInt());
        String lang = "en" + rand;
        return String.format(URL_TEMPLATE, url, locale, lang);

    }

    public String getUrl(){
        return monitorUrl;
    }
}
