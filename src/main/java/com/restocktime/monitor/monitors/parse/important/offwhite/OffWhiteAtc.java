package com.restocktime.monitor.monitors.parse.important.offwhite;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.http.request.wrapper.CloudflareRequestWrapper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.important.offwhite.parse.OffWhiteAtcResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

import java.util.Arrays;
import java.util.List;

public class OffWhiteAtc extends AbstractMonitor {

    private final String ATC_LINK_TEMPLATE = "https://www.off---white.com/en/%s/orders/populate.json";
    private String atcLink;
    private final String BODY_TEMPLATE = "{\"variant_id\":%s,\"quantity\":1}";
    private String body;
    private String sku;
    private String region;
    private String url;
    private int idx;

    List<String> variants;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private CloudflareRequestWrapper httpRequestHelper;
    private OffWhiteAtcResponseParser offWhiteAtcResponseParser;

    public OffWhiteAtc(String url, String sku, String region, int delay, AttachmentCreater attachmentCreater, AbstractHttpRequestHelper httpRequestHelper, OffWhiteAtcResponseParser offWhiteAtcResponseParser) {
        this.url = url;
        this.delay = delay;
        this.sku = sku;
        this.region = region;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = (CloudflareRequestWrapper)httpRequestHelper;
        this.offWhiteAtcResponseParser = offWhiteAtcResponseParser;
        this.atcLink = String.format(ATC_LINK_TEMPLATE, region);
        variants = Arrays.asList(sku.split(";"));
        this.idx = 0;


    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        if(isFirst){
            basicRequestClient.getHeaderList().add(new BasicHeader("Content-Type", "application/json"));
        }

        try {

            BasicHttpResponse response = httpRequestHelper.performPost(basicRequestClient, atcLink, String.format(BODY_TEMPLATE, variants.get(idx)));
            offWhiteAtcResponseParser.parse(response, attachmentCreater, isFirst);
            idx = (idx + 1) % variants.size();
            Notifications.send(attachmentCreater);
            for(int i = 0; i < basicRequestClient.getCookieStore().getCookies().size(); i++){
                if(basicRequestClient.getCookieStore().getCookies().get(i).getName().equals("guest_token")){
                    BasicClientCookie basicClientCookie = new BasicClientCookie("guest_token", "");
                    basicClientCookie.setDomain("www.off---white.com");
                    basicRequestClient.getCookieStore().addCookie(basicClientCookie);
                }
            }
        } catch(Exception e){

        }
    }

    public String getUrl(){
        return url;
    }
}