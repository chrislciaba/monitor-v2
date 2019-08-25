package com.restocktime.monitor.monitors.ingest.important.svd;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.important.svd.parse.SvdProductResponseParser;
import com.restocktime.monitor.monitors.parse.important.svd.parse.SvdSearchAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class SVD extends AbstractMonitor {
    final static Logger log = Logger.getLogger(SVD.class);

    private String url;
    private String sku;
    private boolean isSearch;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private Notifications notifications;
    private SvdProductResponseParser svdProductResponseParser;
    private SvdSearchAbstractResponseParser svdSearchResponseParser;

    public SVD(String url, String sku, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, SvdProductResponseParser svdProductResponseParser, SvdSearchAbstractResponseParser svdSearchResponseParser){
        this.url = url;
        this.sku = sku;
        isSearch = url.contains("popupview");
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.notifications = notifications;
        this.svdProductResponseParser = svdProductResponseParser;
        this.svdSearchResponseParser = svdSearchResponseParser;
    }
    //https://www.sivasdescalzo.com/en/catalog/product/popupview/product/

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            if(isSearch) {
                svdSearchResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            } else {
                svdProductResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            }
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }

    }

    public String getUrl(){
        return url;
    }
}