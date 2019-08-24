package com.restocktime.monitor.monitors.ingest;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.hash.MD5;
import com.restocktime.monitor.util.httprequests.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import lombok.Builder;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

@Builder
public class Http2DefaultMonitor extends AbstractMonitor {
        final static Logger log = Logger.getLogger(Http2DefaultMonitor.class);

        private String url;
        private int delay;
        private AttachmentCreater attachmentCreater;
        private AbstractHttpRequestHelper httpRequestHelper;
        private AbstractResponseParser abstractResponseParser;
        private String hash;


        public void run(BasicRequestClient basicRequestClient, boolean isFirst) {
            attachmentCreater.clearAll();
            Timeout.timeout(delay);

            try{
                BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
                if (basicHttpResponse.getBody().isPresent()) {
                    String md5 = MD5.getMd5(basicHttpResponse.getBody().get());
                    if (md5.equals(hash)) {
                        return;
                    }

                    hash = md5;
                }


                abstractResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
                Notifications.send(attachmentCreater);
            } catch(Exception e){
                log.error(EXCEPTION_LOG_MESSAGE, e);
            }
        }

        public String getUrl(){
            return url;
        }
}
