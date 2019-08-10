package com.restocktime.monitor.monitors.ingest.demandware;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.bstn.BSTN;
import com.restocktime.monitor.monitors.parse.demandware.parse.DemandwareGetResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class DemandwareGet extends AbstractMonitor {
    private String site;
    private String sitesSite;
    private final String ATC_REQUEST_TEMPLATE = "https://www.%s.com/on/demandware.store/Sites-%s-Site/default/Product-Variation?pid=%s&dwvar_%s_color=%s&dwvar_%s_size=%s&Quantity=%s&format=ajax";
    private final String STOCK_REQUEST_TEMPLATE = "https://www.%s.com/on/demandware.store/Sites-%s-Site/default/Product-GetAvailability?pid=%s";
    private final String FYE_STOCK_TEMPLATE = "https://www.%s.com/on/demandware.store/Sites-%s-Site/default/Product-GetAvailability?pid=%s&Quantity=999999";
    private final String LINK_TEMPLATE_HT = "https://www.hottopic.com/product/%s.html";
    private final String LINK_TEMPLATE_BL = "https://www.boxlunch.com/product/%s.html";
    private final String LINK_TEMPLATE_FYE = "https://www.fye.com/product/%s.html";

    private static final Logger log = Logger.getLogger(BSTN.class);


    private String LINK_TEMPLATE;
    private String STOCK_TEMPLATE;
    private String url;
    private String sku;
    private int delay;
    private String size;
    private String color;

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private DemandwareGetResponseParser demandwareGetResponseParser;

    public DemandwareGet(String url, String sku, int delay,  AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, DemandwareGetResponseParser demandwareGetResponseParser) {
        this.url = url;
        if(sku.contains(":")){
            String[] skuParts = sku.split(":");
            this.sku = skuParts[0];
            this.size = skuParts[1];
            this.color = "MULTI";
        } else {
            this.sku = sku;
            this.size = "";
            this.color = "";
        }
        this.delay = delay;
        if(url.contains("hottopic")){
            site = "hottopic";
            sitesSite = "hottopic";
            LINK_TEMPLATE = LINK_TEMPLATE_HT;
            STOCK_TEMPLATE = STOCK_REQUEST_TEMPLATE;

        } else if(url.contains("boxlunch")) {
            site = "boxlunch";
            sitesSite = "boxlunch";
            LINK_TEMPLATE = LINK_TEMPLATE_BL;
            STOCK_TEMPLATE = STOCK_REQUEST_TEMPLATE;
        } else if(url.contains("fye")) {
            site = "fye";
            sitesSite = "FYE";
            LINK_TEMPLATE = LINK_TEMPLATE_FYE;
            STOCK_TEMPLATE = FYE_STOCK_TEMPLATE;
        }
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.demandwareGetResponseParser = demandwareGetResponseParser;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        String url = String.format(LINK_TEMPLATE, sku);
        int min = 100000;
        int max = 600000;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

        String atcRequestLink = String.format(ATC_REQUEST_TEMPLATE, site, sitesSite, sku, sku, color, sku, size, randomNum);
        String availabilityRequestLink = String.format(STOCK_TEMPLATE, site, sitesSite, sku);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, atcRequestLink);
            BasicHttpResponse stockHttpResponse = httpRequestHelper.performGet(basicRequestClient, availabilityRequestLink);
            demandwareGetResponseParser.setUrl(url);
            demandwareGetResponseParser.setSku(sku);
            demandwareGetResponseParser.parse(basicHttpResponse,/* stockHttpResponse,*/ attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch (Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
