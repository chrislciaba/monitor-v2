package com.restocktime.monitor.monitors.ingest.barnesandnoble;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.barnesandnoble.parse.BarnesAndNobleResponseParser;
import com.restocktime.monitor.monitors.ingest.shopify.Shopify;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.util.*;

public class BarnesAndNoble extends AbstractMonitor {
    private String url;
    private String prodUrl;
    private int delay;
    final static Logger logger = Logger.getLogger(Shopify.class);
    private final String apiUrl = "https://m.barnesandnoble.com/skavastream/core/v5/barnesandnobleapi/productdetails/get?campaignId=1&productids=%s";
    private final String productUrl = "https://www.barnesandnoble.com/w/jarman/123?ean=%s";

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private BarnesAndNobleResponseParser barnesAndNobleResponseParser;

    public BarnesAndNoble(String sku, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper,BarnesAndNobleResponseParser barnesAndNobleResponseParser){
        this.url = String.format(apiUrl, sku);
        this.prodUrl = String.format(productUrl, sku);
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.barnesAndNobleResponseParser = barnesAndNobleResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);
        List<Header> h = new ArrayList<>();
        h.add(new BasicHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 SKAVA_NATIVE_IOS SkavaiPhoneApp"));
        h.add(new BasicHeader("Referer", "https://m.barnesandnoble.com/"));
        h.add(new BasicHeader("Accept-Language", "en-us"));
        h.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
        basicRequestClient.setHeaderList(h);

        try {
            //BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            BasicHttpResponse prodHttpResponse = httpRequestHelper.performGet(basicRequestClient, prodUrl);
            barnesAndNobleResponseParser.parse(basicHttpResponse, prodHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.info(e);
        }
    }

    public String getUrl(){
        return url;
    }
}