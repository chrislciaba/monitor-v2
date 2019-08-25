package com.restocktime.monitor.monitors.ingest.important.footdistrict;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.footdistrict.helper.BotBypass;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class FootDistrict extends AbstractMonitor {//<h2 class="product-name"><a href="https://footdistrict.com/air-jordan-1-retro-high-og-555088-801.html" title="Air Jordan 1 Retro High OG">Air Jordan 1 Retro High OG</a>

    final static Logger log = Logger.getLogger(FootDistrict.class);

    private String url;
    private int delay;

    Map<String, String> cookieList;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private BotBypass botBypass;
    private AbstractResponseParser abstractResponseParser;

    public FootDistrict(String url, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, BotBypass botBypass, AbstractResponseParser abstractResponseParser){
        this.url = url;
        this.delay = delay;
        this.cookieList = new HashMap<>();
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.botBypass = botBypass;
        this.abstractResponseParser = abstractResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        try{

            List<Header> headerList = new ArrayList<>();
            String cookie = "";
            if(basicRequestClient.getHttpHost() !=  null && basicRequestClient.getHttpHost().getHostName() != null && cookieList.containsKey(basicRequestClient.getHttpHost().getHostName())){
                cookie = cookieList.get(basicRequestClient.getHttpHost().getHostName());
            }

            headerList.add(new BasicHeader("Cookie", cookie));
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.urlWithRandParam(url));
            BasicHttpResponse basicHttpResponseAfterBot = botBypass.bypassBotProtection(httpRequestHelper, basicRequestClient, basicHttpResponse, UrlHelper.urlWithRandParam(url));

            abstractResponseParser.parse(basicHttpResponseAfterBot, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }

}
