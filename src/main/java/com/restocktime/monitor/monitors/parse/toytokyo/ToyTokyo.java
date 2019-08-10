package com.restocktime.monitor.monitors.parse.toytokyo;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;


public class ToyTokyo extends AbstractMonitor {
    final static Logger logger = Logger.getLogger(ToyTokyo.class);

    private String url;
    private int delay;

    private String[] discordWebhook;
    private SlackObj[] slackObj;
    private boolean isFirstRun;
    private String productId;
    private String name;
    private final String ATC_TEMPLATE = "{\"action\":\"add\",\"product_id\":%s,\"qty[]\":1}";
    private String atcReq;
    private final String ATC_URL = "https://www.toytokyo.com/remote/v1/cart/add";
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private List<String> formatNames;

    public ToyTokyo(String url, int delay,  SlackObj[] slackObj, String[] discordWebhook, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper){
        this.url = url;
        this.delay = delay;
        this.slackObj = slackObj;
        this.discordWebhook = discordWebhook;
        isFirstRun = true;
        atcReq = null;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        /*attachmentCreater.clearAll();
        String patternStr = "<title>(.*)</title>";
        Pattern pattern = Pattern.compile(patternStr);
        String prodIdStr = "wishlist\\.php\\?action=add&amp;product_id=([0-9]*)";
        Pattern prodIdPattern = Pattern.compile(prodIdStr);

        if(isFirstRun) {

            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(httpClient, config, null, url, null);
            if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {
                return notify;
            }

            Matcher prodIdMatcher = prodIdPattern.matcher(basicHttpResponse.getBody());
            Matcher patternMatcher = pattern.matcher(basicHttpResponse.getBody());

            if (prodIdMatcher.find() && patternMatcher.find()) {
                this.productId = prodIdMatcher.group(1);
                this.name = patternMatcher.group(1);
                atcReq = String.format(ATC_TEMPLATE, productId);
            }
        }

        isFirstRun = false;

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //notify slack
        }

        try{
            List<Header> headerList = new ArrayList<>();
            headerList.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            BasicHttpResponse basicHttpResponse = httpRequestHelper.performPost(httpClient, config, null, ATC_URL, headerList, atcReq);
            if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {
                return notify;
            }

            if(basicHttpResponse.getBody().contains("cart_item")){
                logger.info("In stock");
                if(notify){
                    attachmentCreater.addMessages(url, name, "ToyTokyo", null, null);
                    //Notifications.send(discordWebhook, slackObj, attachmentCreater);
                }

                return false;
            } else if(basicHttpResponse.getResponseCode() >= 400){
                return notify;
            } else {
                logger.info("oos - " + name);
                return true;
            }


        } catch(Exception e){
            logger.error(e);
            return notify;
        }*/
    }

    public String getUrl(){
        return url;
    }
}
