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

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;


public class Titolo extends AbstractMonitor {
    final static Logger log = Logger.getLogger(Titolo.class);

    private String url;
    private boolean isSearch;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private TitoloSearchResponseParser titoloSearchResponseParser;
    private TitoloProductResponseParser titoloProductResponseParser;


    public Titolo(String url, String sku, String fallbackName,  int delay,  AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, TitoloProductResponseParser titoloProductResponseParser, TitoloSearchResponseParser titoloSearchResponseParser){
        this.url = url;
        isSearch = url.contains("catalogsearch");
        this.delay = delay;
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
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
