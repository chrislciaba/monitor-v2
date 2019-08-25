package com.restocktime.monitor.monitors.parse.important.endclothing;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class EndClothing extends AbstractMonitor {
    private String url;
    private int delay;

    private String[] discordWebhook;
    private SlackObj[] slackObj;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private List<String> formatNames;

    public EndClothing(String url, int delay,  SlackObj[] slackObj, String[] discordWebhook){
        this.url = url;
        this.delay = delay;
        this.slackObj = slackObj;
        this.discordWebhook = discordWebhook;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;

    }

    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        /*
        String patternStr = "<title>(.*)</title>";
        Pattern pattern = Pattern.compile(patternStr);
        HttpGet request = new HttpGet(url);
        HttpClientContext context = HttpClientContext.create();
        request.setConfig(config);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //notify slack
        }

        try{
            HttpResponse response = httpClient.execute(request, context);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            responseString = responseString.toLowerCase();
            System.out.println(responseString);
            if(responseString != null && responseString.contains("product-addtocart-button")){

                Matcher m = pattern.matcher(responseString);
                String productName = "";
                if (m.find()) {
                    productName = m.group(1).replaceAll("\\s+"," ").trim().toUpperCase();
                }

                if(notify) {
                    //Slack.send(url, productName, "End TESTING NO ONE FREAK OUT", null, slackConfig.getEndclothing());
                    attachmentCreater.addMessages(url, productName, "Naked", null, null);
                   // Notifications.send(discordWebhook, slackObj, attachmentCreater);
                }
                exit(1);
                return false;
            } else {

                return true;
            }

        } catch(Exception e){
            return notify;
        }*/
    }

    public String getUrl(){
        return url;
    }
}
