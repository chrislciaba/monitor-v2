package com.restocktime.monitor.monitors.ingest;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.helper.hash.MD5;
import com.restocktime.monitor.util.http.request.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.ops.log.DiscordLog;
import com.restocktime.monitor.util.ops.log.WebhookType;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import lombok.Builder;
import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

@Builder
public class Http2DefaultMonitor extends AbstractMonitor {
        final static Logger log = Logger.getLogger(Http2DefaultMonitor.class);

        private String url;
        private boolean addUUID;
        private int delay;
        private AttachmentCreater attachmentCreater;
        private AbstractHttpRequestHelper httpRequestHelper;
        private AbstractResponseParser abstractResponseParser;
        private String hash;


        public void run(BasicRequestClient basicRequestClient, boolean isFirst) {
            DiscordLog.log(WebhookType.OTHER, Thread.currentThread().getName() + ": start of monitor");
            attachmentCreater.clearAll();
            Timeout.timeout(delay);
            String cacheUrl = url;
            if(addUUID){
                cacheUrl += UUID.randomUUID();
            }

            try{
                long t0 = System.currentTimeMillis();
                BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, cacheUrl);

                long t1 = System.currentTimeMillis();
                if (basicHttpResponse.getBody().isPresent()) {
                    System.out.println(basicHttpResponse.getBody().get());

                    String md5 = MD5.getMd5(basicHttpResponse.getBody().get());
                    if (md5.equals(hash)) {
                        DiscordLog.log(WebhookType.OTHER, Thread.currentThread().getName() + ": Equal to last response, ignoring");
                        return;
                    }

                    hash = md5;
                }


                abstractResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
                Notifications.send(attachmentCreater);
                DiscordLog.log(WebhookType.OTHER, Thread.currentThread().getName() + ": Total time in monitor 2: " + ((t1-t0)/1000));
            } catch(Exception e){
                DiscordLog.log(WebhookType.OTHER, Thread.currentThread().getName() + ": " + e.getMessage());
                log.error(EXCEPTION_LOG_MESSAGE, e);
            }
        }

        public String getUrl(){
            return url;
        }
}
