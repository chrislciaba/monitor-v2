package com.restocktime.monitor.monitors.ingest.important.snkrs;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.bot.protection.nikefrontendlogin.NikeLogin;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.important.snkrs.parse.HuntResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class NikeScratch extends AbstractMonitor {
    final static Logger log = Logger.getLogger(NikeScratch.class);
    private String url;
    private int delay;
    private List<Header> headers;
    private String token;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private HuntResponseParser huntResponseParser;
    private long nextRefresh;

    public NikeScratch(String url, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, HuntResponseParser huntResponseParser) {
        this.url = url;
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.huntResponseParser = huntResponseParser;
        this.nextRefresh = 0;
        this.token = null;


        headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
        headers.add(new BasicHeader("X-NewRelic-ID", "VQYGVF5SCBAJVlFaAQIH"));
        headers.add(new BasicHeader("Origin", "https://s3.nikecdn.com"));
        headers.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36"));
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));
        headers.add(new BasicHeader(HttpHeaders.REFERER, "https://s3.nikecdn.com/unite/mobile.html"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.9,ms;q=0.8"));
    }



    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();

        Timeout.timeout(delay);

        try{
            if(nextRefresh < System.currentTimeMillis() || token == null){
                tokenUpdate();
            }

            List<Header> httpHeaderForMain = new ArrayList<>();
            httpHeaderForMain.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token));
            basicRequestClient.setHeaderList(httpHeaderForMain);
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);

            if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
                return;
            }

            huntResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);


        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    private void tokenUpdate() {
        token = null;
        while (token == null) {
            token = NikeLogin.getLoginFrontend();
            nextRefresh = System.currentTimeMillis() + 3540000; //+59 minutes
        }
    }


    public String getUrl(){
        return url;
    }
}
