package com.restocktime.monitor.monitors.ingest.shopify;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.hash.MD5;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyProductsResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.Random;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ShopifyProducts extends AbstractMonitor {
    private static final String PRODUCT_LISTINGS_TEMPLATE = "%s/products.json?limit=";

    final static Logger log = Logger.getLogger(ShopifyProducts.class);

    private String url;
    private int delay;

    private Random r;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private ShopifyProductsResponseParser shopifyProductsResponseParser;
    private String hash;

    public ShopifyProducts(String url, int delay,  AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, ShopifyProductsResponseParser shopifyProductsResponseParser){
        this.url = String.format(PRODUCT_LISTINGS_TEMPLATE, UrlHelper.deriveBaseUrl(url));
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.shopifyProductsResponseParser = shopifyProductsResponseParser;
        this.r = new Random();
        this.hash = "";
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url + (25 + new Random().nextInt(100000)));

            String md5 = MD5.getMd5(basicHttpResponse.getBody());
            if (hash.equals(md5)) {
                return;
            }
            shopifyProductsResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
            hash  = md5;
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);

        }

    }


    public String getUrl(){
        return url;
    }
}
