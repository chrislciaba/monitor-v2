package com.restocktime.monitor.monitors.ingest.shopify;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.hash.MD5;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyKwAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ShopifyKw extends AbstractMonitor {

    final static Logger log = Logger.getLogger(ShopifyKw.class);

    private String url;
    private int delay;
    private Random r;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private ShopifyKwAbstractResponseParser shopifyKwResponseParser;
    private String hash;

    public ShopifyKw(String url, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, ShopifyKwAbstractResponseParser shopifyKwResponseParser){
        this.url = UrlHelper.deriveBaseUrl(url) + "/sitemap_products_1.xml?from=%d&to=%d";
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.shopifyKwResponseParser = shopifyKwResponseParser;
        this.r = new Random();
        this.hash = "";
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, String.format(url, ThreadLocalRandom.current().nextInt(1, 3000), 9223372036854775807L));
            String md5 = MD5.getMd5(basicHttpResponse.getBody().get());

            if (hash.equals(md5)) {
                return;
            }

            shopifyKwResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
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
