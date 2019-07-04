package com.restocktime.monitor.monitors.ingest.oneblockdown;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.jimmyjazz.JimmyJazz;
import com.restocktime.monitor.monitors.parse.oneblockdown.parse.OneBlockDownResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

public class OneBlockDown extends AbstractMonitor {

    private String url;
    private int delay;
    final static Logger logger = Logger.getLogger(JimmyJazz.class);

    private AttachmentCreater attachmentCreater;
    private CloudflareRequestHelper cloudflareRequestHelper;
    private OneBlockDownResponseParser oneBlockDownResponseParser;
    private String[] skus;
    private int idx;
    private final String BODY_TEMPLATE = "controller=orders&action=addStockItemToBasket&stockItemId=%s&quantity=1&extension=obd&version=94";

    public OneBlockDown(String url, String skus, int delay, AttachmentCreater attachmentCreater, CloudflareRequestHelper cloudflareRequestHelper, OneBlockDownResponseParser oneBlockDownResponseParser){
        this.url = url;
        this.skus = skus.split(":");
        this.idx = 0;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.cloudflareRequestHelper = cloudflareRequestHelper;
        this.oneBlockDownResponseParser = oneBlockDownResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);
        String sku = skus[idx];
        try {
            basicRequestClient.getHeaderList().clear();
            BasicHttpResponse basicHttpResponse;// = cloudflareRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));
            basicRequestClient.getHeaderList().add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
            basicRequestClient.getHeaderList().add(new BasicHeader("x-requested-with", "XMLHttpRequest"));
            basicHttpResponse = cloudflareRequestHelper.performPost(basicRequestClient, "https://www.oneblockdown.it/index.php", String.format(BODY_TEMPLATE, sku));
            oneBlockDownResponseParser.setCurSku(sku);
            oneBlockDownResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            //basicHttpResponse = cloudflareRequestHelper.performPost(basicRequestClient, "https://www.oneblockdown.it/index.php", "controller=orders&action=removeStockItemToBasket&stockItemId=60756&quantity=1&extension=obd&version=94");
            logger.info(attachmentCreater.isEmpty());
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.info(e);

        }
        idx = (idx + 1) % skus.length;
    }

    public String getUrl(){
        return url;
    }
}
