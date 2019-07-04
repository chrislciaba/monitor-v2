package com.restocktime.monitor.monitors.ingest.titolo;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.titolo.parse.TitoloProductResponseParser;
import com.restocktime.monitor.monitors.parse.titolo.parse.TitoloSearchResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;


public class Titolo extends AbstractMonitor {
    final static Logger logger = Logger.getLogger(Titolo.class);

    private String url;
    private String sku;
    private boolean isSearch;
    private int delay;
    private String fallbackName;
    private String[] discordWebhook;
    private SlackObj[] slackObj;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private TitoloSearchResponseParser titoloSearchResponseParser;
    private TitoloProductResponseParser titoloProductResponseParser;
    private Notifications notifications;


    public Titolo(String url, String sku, String fallbackName,  int delay,  AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, TitoloProductResponseParser titoloProductResponseParser, TitoloSearchResponseParser titoloSearchResponseParser){
        this.url = url;
        this.sku = sku;
        isSearch = url.contains("catalogsearch");
        this.delay = delay;
        this.fallbackName = fallbackName;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.titoloProductResponseParser = titoloProductResponseParser;
        this.titoloSearchResponseParser = titoloSearchResponseParser;
    }
    //https://www.sivasdescalzo.com/en/catalog/product/popupview/product/

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            if (isSearch) {
                titoloSearchResponseParser.parse(basicHttpResponse, attachmentCreater,isFirst);
            } else {
                titoloProductResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            }
            Notifications.send(attachmentCreater);

        } catch (Exception e) {

        }
    }

    public String getUrl(){
        return url;
    }
}
