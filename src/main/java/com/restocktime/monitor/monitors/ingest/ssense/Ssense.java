package com.restocktime.monitor.monitors.ingest.ssense;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.ssense.parse.PageResponseParser;
import com.restocktime.monitor.monitors.parse.ssense.parse.SearchResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Ssense extends AbstractMonitor {
    //<span class="button-label">Add to bag</span>

    final static Logger logger = Logger.getLogger(Ssense.class);

    private String url;
    private int delay;
    private String locale;
    Map<String, String> urls;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private PageResponseParser pageResponseParser;
    private SearchResponseParser searchResponseParser;

    public Ssense(String url, int delay,  String locale,  AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, PageResponseParser pageResponseParser, SearchResponseParser searchResponseParser){
        this.url = url;
        this.delay = delay;
        this.locale = locale;
        urls = new HashMap<>();
        urls.put("CA", "https://www.ssense.com/en-ca");
        urls.put("US", "https://www.ssense.com/en-us");
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.pageResponseParser = pageResponseParser;
        this.searchResponseParser = searchResponseParser;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));
        if(url.contains("?q=")){
            searchResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
        } else {
            pageResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
        }
        Notifications.send(attachmentCreater);
    }

    public String getUrl(){
        return url;
    }
}
