package com.restocktime.monitor.monitors.ingest.shopify;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.MD5;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.shopify.helper.ShopifyFrontendHelper;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyAbstractResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ShopifyFrontend extends AbstractMonitor {
    private String url;
    private int delay;
    final static Logger log = Logger.getLogger(ShopifyFrontend.class);
    private Random r;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private ShopifyAbstractResponseParser shopifyResponseParser;
    private String hash;
    private ShopifyFrontendHelper shopifyFrontendHelper;

    public ShopifyFrontend(String url, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, ShopifyAbstractResponseParser shopifyResponseParser, ShopifyFrontendHelper shopifyFrontendHelper){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.shopifyResponseParser = shopifyResponseParser;
        this.r = new Random();
        this.hash = "";
        this.shopifyFrontendHelper = shopifyFrontendHelper;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url + "?limit=" + Math.abs(r.nextLong()));
            String md5 = MD5.getMd5(basicHttpResponse.getBody().get());
            if (hash.equals(md5)) {
                return;
            }

            List<String> links = shopifyFrontendHelper.findLinks(basicHttpResponse, isFirst);
            for (String link : links) {
                attachmentCreater.clearAll();
                basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, link + "?format=js&limit=" + Math.abs(r.nextLong()));
                shopifyResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
                Notifications.send(attachmentCreater);
            }

            hash = md5;
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
