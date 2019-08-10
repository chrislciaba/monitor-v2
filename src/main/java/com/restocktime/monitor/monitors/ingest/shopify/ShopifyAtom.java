package com.restocktime.monitor.monitors.ingest.shopify;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.MD5;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyAtomResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.Random;

public class ShopifyAtom extends AbstractMonitor {
    final static Logger logger = Logger.getLogger(ShopifyAtom.class);

    private String url;
    private int delay;

    private Random r;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private ShopifyAtomResponseParser shopifyAtomResponseParser;
    private String hash;

    public ShopifyAtom(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, ShopifyAtomResponseParser shopifyAtomResponseParser){
        this.url = UrlHelper.deriveBaseUrl(url) + "/collections/all?format=atom&limit=";
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.shopifyAtomResponseParser = shopifyAtomResponseParser;
        this.r = new Random();
        this.hash = "";
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url + Long.toString(Math.abs(r.nextLong())) + Long.toString(Math.abs(r.nextLong())));

            String md5 = MD5.getMd5(basicHttpResponse.getBody().get());
            if (hash.equals(md5)) {
                return;
            }
            shopifyAtomResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.info(e);
        }

    }

    public String getUrl(){
        return url;
    }
}
