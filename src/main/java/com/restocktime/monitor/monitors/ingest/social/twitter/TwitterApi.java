package com.restocktime.monitor.monitors.ingest.social.twitter;

import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.DefaultMonitor;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.util.bot.protection.twitter.login.TwitterLoginHelper;
import com.restocktime.monitor.util.helper.hash.MD5;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class TwitterApi extends AbstractMonitor {
    final static Logger log = Logger.getLogger(DefaultMonitor.class);

    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private AbstractHttpRequestHelper httpRequestHelper;
    private AbstractResponseParser abstractResponseParser;
    private String hash;
    private Map<String, String> headers;

    public TwitterApi(
            String url,
            int delay,
            AttachmentCreater attachmentCreater,
            AbstractHttpRequestHelper httpRequestHelper,
            AbstractResponseParser abstractResponseParser,
            String hash) {
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.abstractResponseParser = abstractResponseParser;
        this.hash = hash;
        this.headers = null;
    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst) {
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);

            if(basicHttpResponse.getResponseCode().isPresent() && basicHttpResponse.getResponseCode().get() != 200){
                Map<String, String> headers = TwitterLoginHelper.getLoginFrontend();

                List<Header> headerList = new ArrayList<>();
                for(Header header : basicRequestClient.getHeaderList()){
                    if (!header.getName().equals("x-guest-token") && !header.getName().equals("x-csrf-token")) {
                        headerList.add(header);
                    }
                }

                headerList.add(new BasicHeader("x-guest-token", headers.get("gt")));
                headerList.add(new BasicHeader("x-csrf-token", headers.get("ct0")));

                basicRequestClient.setHeaderList(headerList);
                Timeout.timeout(delay);
                basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            }

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
