package com.restocktime.monitor.monitors.ingest.important.ssense;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.important.ssense.parse.PageResponseParser;
import com.restocktime.monitor.monitors.parse.important.ssense.parse.SearchResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Ssense extends AbstractMonitor {
    //<span class="button-label">Add to bag</span>

    final static Logger log = Logger.getLogger(Ssense.class);

    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private PageResponseParser pageResponseParser;
    private SearchResponseParser searchResponseParser;

    public Ssense(String url, int delay,  String locale,  AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, PageResponseParser pageResponseParser, SearchResponseParser searchResponseParser){
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.pageResponseParser = pageResponseParser;
        this.searchResponseParser = searchResponseParser;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        try {
            attachmentCreater.clearAll();
            Timeout.timeout(delay);

            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));
            if(url.contains("?q=")){
                searchResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            } else {
                pageResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            }
            Notifications.send(attachmentCreater);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }

    }

    public String getUrl(){
        return url;
    }
}