package com.restocktime.monitor.monitors.ingest.shopify;

import com.restocktime.monitor.helper.botstarters.QuicktaskConfig;
import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.hash.MD5;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.Random;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Shopify extends AbstractMonitor {
    private String url;
    private String jsonUrl;
    private int delay;
    final static Logger log = Logger.getLogger(Shopify.class);
    private Random r;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private ShopifyAbstractResponseParser shopifyResponseParser;
    private QuicktaskConfig quicktaskConfig;
    private String hash;

    public Shopify(String url, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, ShopifyAbstractResponseParser shopifyResponseParser, QuicktaskConfig quicktaskConfig){
        this.url = url;
        this.jsonUrl = String.format("%s?format=js&limit=", url);
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.shopifyResponseParser = shopifyResponseParser;
        this.quicktaskConfig = quicktaskConfig;
        this.r = new Random();
        this.hash = "";
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, jsonUrl + Long.toString(Math.abs(r.nextLong())) + Long.toString(Math.abs(r.nextLong())));
            String md5 = MD5.getMd5(basicHttpResponse.getBody().get());
            if (hash.equals(md5)) {
                return;
            }

            shopifyResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
            hash = md5;
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
