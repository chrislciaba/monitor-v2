package com.restocktime.monitor.monitors.ingest.disney;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.disney.parse.DisneySitemapResponseParser;
import com.restocktime.monitor.monitors.ingest.shopify.Shopify;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class DisneySitemap extends AbstractMonitor {
    private String url;
    private int delay;
    final static Logger log = Logger.getLogger(DisneySitemap.class);
    private final Pattern pattern = Pattern.compile("<loc>([^<]*)</loc>");

    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private DisneySitemapResponseParser disneySitemapResponseParser;

    public DisneySitemap(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper,DisneySitemapResponseParser disneySitemapResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.disneySitemapResponseParser = disneySitemapResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.deriveBaseUrl(url) + "/sitemap.xml");
            Matcher m = pattern.matcher(basicHttpResponse.getBody().get());
            while(m.find()){
                Timeout.timeout(delay);
                basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, m.group(1));
                disneySitemapResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            }
            basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));
            disneySitemapResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.info(EXCEPTION_LOG_MESSAGE, e);

        }
    }

    public String getUrl(){
        return url;
    }
}
