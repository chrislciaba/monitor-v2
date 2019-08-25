package com.restocktime.monitor.monitors.ingest.important.footsites;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.important.footsites.helper.SkuHelper;
import com.restocktime.monitor.monitors.parse.important.footsites.parse.FootsitesResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Footsites extends AbstractMonitor {

    private String url;
    private String monitorUrl;
    private int delay;
    final static Logger log = Logger.getLogger(Footsites.class);

    private final String  URL_TEMPLATE = "%s/api/products/pdp/%s?format=json";
    private final Pattern skuPattern = Pattern.compile("/([^.]*)\\.html");


    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private FootsitesResponseParser footsitesResponseParser;

    public Footsites(String url, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, FootsitesResponseParser footsitesResponseParser){
        this.url = url;
        this.monitorUrl = buildUrl(url);
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.footsitesResponseParser = footsitesResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, monitorUrl);
            footsitesResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    private String buildUrl(String productUrl){
        String baseUrl = UrlHelper.deriveBaseUrl(productUrl);
        String sku = SkuHelper.getSku(productUrl);
        if(sku != null)
            return String.format(URL_TEMPLATE, baseUrl, sku);

        return null;
    }

    public String getUrl(){
        return url;
    }
}
