package com.restocktime.monitor.monitors.ingest.bstn;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.barnesandnoble.BarnesAndNoble;
import com.restocktime.monitor.monitors.parse.bstn.parse.BSTNParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.bstn.parse.BSTNParseSearchAbstractResponse;
import com.restocktime.monitor.monitors.parse.bstn.parse.BstnParsePageResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class BSTN extends AbstractMonitor {

    private static final Logger log = Logger.getLogger(BSTN.class);

    private String url;
    private String sku;
    private int delay;
    private boolean isSearch;

    private AttachmentCreater attachmentCreater;
    private CloudflareRequestHelper cloudflareRequestHelper;
    private Notifications notifications;
    private BSTNParseSearchAbstractResponse bstnParseSearchResponse;
    private BSTNParseProductAbstractResponse bstnParseProductResponse;
    private BstnParsePageResponse bstnParsePageResponse;

    public BSTN(String url, String sku, int delay, AttachmentCreater attachmentCreater, CloudflareRequestHelper cloudflareRequestHelper, BSTNParseProductAbstractResponse bstnParseProductResponse, BSTNParseSearchAbstractResponse bstnParseSearchResponse, BstnParsePageResponse bstnParsePageResponse){
        this.url = url;
        this.sku = sku;
        this.delay = delay;
        isSearch = url.contains("searchstring");
        this.attachmentCreater = attachmentCreater;
        this.cloudflareRequestHelper = cloudflareRequestHelper;
        this.bstnParseProductResponse = bstnParseProductResponse;
        this.bstnParseSearchResponse = bstnParseSearchResponse;
        this.bstnParsePageResponse = bstnParsePageResponse;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse response = cloudflareRequestHelper.performGet(basicRequestClient, url);

            if(isSearch) {
                bstnParseSearchResponse.parse(response, attachmentCreater, isFirst);
            } else if(url.contains("/p/")){
                bstnParseProductResponse.parse(response, attachmentCreater, isFirst);
            } else {
                bstnParsePageResponse.parse(response, attachmentCreater, isFirst);
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
