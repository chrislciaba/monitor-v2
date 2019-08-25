package com.restocktime.monitor.monitors.ingest.important.shopify;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.important.shopify.helper.CheckoutLinkRedirectHelper;
import com.restocktime.monitor.monitors.parse.important.shopify.helper.NameChangeHelper;
import com.restocktime.monitor.monitors.parse.important.shopify.parse.ShopifyKwAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class CheckoutLinkAbstractMonitor extends AbstractMonitor {
    private List<String> urls;
    private String sitemapUrl;
    private int delay;
    final static Logger logger = Logger.getLogger(Shopify.class);

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private ShopifyKwAbstractResponseParser shopifyKwResponseParser;
   // private Notifications notifications;
    private Map<String, String> linkChangeNames;
    private NameChangeHelper nameChangeHelper;
    private int idx;

    public CheckoutLinkAbstractMonitor(List<String> urls, String sitemapUrl, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, Map<String, String> linkChangeNames, NameChangeHelper nameChangeHelper, ShopifyKwAbstractResponseParser shopifyKwResponseParser){
        this.urls = urls;
        this.sitemapUrl = sitemapUrl;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.shopifyKwResponseParser = shopifyKwResponseParser;
   //     this.notifications = notifications;
        this.linkChangeNames = linkChangeNames;
        this.nameChangeHelper = nameChangeHelper;
        this.idx = 0;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            CheckoutLinkRedirectHelper checkoutLinkRedirectHelper = new CheckoutLinkRedirectHelper();
            BasicHttpResponse checkoutHttpResponse = checkoutLinkRedirectHelper.getCheckoutPage(urls.get(idx), basicRequestClient, httpRequestHelper);

            if(nameChangeHelper.parse(checkoutHttpResponse, linkChangeNames, urls.get(idx))){
                shopifyKwResponseParser.updateKeywords(linkChangeNames);
            }
            idx = (idx + 1) % urls.size();
            BasicHttpResponse basicSitemapHttpResponse = httpRequestHelper.performGet(basicRequestClient, sitemapUrl);

            shopifyKwResponseParser.parse(basicSitemapHttpResponse, attachmentCreater, isFirst);
           // Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.info(e);

        }
    }

    public int getNumUrls(){
        return urls.size();
    }

    public String getUrl(){
        return this.urls.get(0);
    }
}
