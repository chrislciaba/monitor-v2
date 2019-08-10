package com.restocktime.monitor.monitors.ingest;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.hash.MD5;
import com.restocktime.monitor.helper.httprequests.AbstractHttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.taskstatus.TaskStatus;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.titolo.Titolo;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class DefaultMonitor extends AbstractMonitor {
    final static Logger log = Logger.getLogger(DefaultMonitor.class);

    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private AbstractHttpRequestHelper httpRequestHelper;
    private AbstractResponseParser abstractResponseParser;
    private String hash;
    private TaskStatus taskStatus;

    public DefaultMonitor(
            String url,
            int delay,
            AttachmentCreater attachmentCreater,
            AbstractHttpRequestHelper httpRequestHelper,
            AbstractResponseParser abstractResponseParser,
            String hash,
            TaskStatus taskStatus) {
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.abstractResponseParser = abstractResponseParser;
        this.hash = hash;
        this.taskStatus = taskStatus;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst) {
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            String md5 = MD5.getMd5(basicHttpResponse.getBody());
            if (md5.equals(hash)) {
                return;
            }

            abstractResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
            hash = md5;
            taskStatus.incrementSuccess();
        } catch(Exception e){
            taskStatus.incrementError();
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
