package com.restocktime.monitor.monitors.ingest.porter;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.jimmyjazz.JimmyJazz;
import com.restocktime.monitor.monitors.parse.porter.parse.ApiAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.porter.parse.AtcResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Porter extends AbstractMonitor {
    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private String locale;
    private ApiAbstractResponseParser apiResponseParser;
    private AtcResponseParser atcResponseParser;
    private Notifications notifications;
    final static Logger log = Logger.getLogger(Porter.class);


    public Porter(String url, String locale, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, ApiAbstractResponseParser apiResponseParser, AtcResponseParser atcResponseParser){
        this.url = url;
        this.locale = locale;
        this.delay = delay;
        this.apiResponseParser = apiResponseParser;
        this.atcResponseParser = atcResponseParser;
        this.notifications = notifications;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            if(url.contains("addsku")){
                atcResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            } else {
                apiResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            }
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
