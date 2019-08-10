package com.restocktime.monitor.monitors.ingest.demandware;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.demandware.parse.DemandwareResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class Demandware extends AbstractMonitor {
    final static Logger logger = Logger.getLogger(Demandware.class);
    private final String ATC_TEMPLATE = "%s/on/demandware.store/Sites-%s-Site/default/Cart-AddProduct?format=ajax";
    private final String ATC_BODY_TEMPLATE = "Quantity=1&cartAction=add&pid=11580712";
    private final String STOCK_TEMPLATE = "%s/on/demandware.store/Sites-%s-Site/default/Product-GetAvailability?";
    private final String STOCK_BODY_TEMPLATE = "pid=11580712";

    private String url;
    private String sku;
    private String region;
    private int delay;
    private Notifications notifications;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private String atcReq;
    private DemandwareResponseParser demandwareResponseParser;

    private List<String> variants;
    private int idx;


    public Demandware(String url, String sku, int delay,   AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, DemandwareResponseParser demandwareResponseParser) {
        this.url = url;
        this.delay = delay;
        this.sku = sku;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.variants = Arrays.asList(sku.split(";"));
        this.idx = 0;
        String site = "";
        if(url.contains("boxlunch.com")){
            site = "boxlunch";
        } else if(url.contains("hottopic.com")){
            site = "hottopic";
        }
        this.atcReq = String.format(ATC_TEMPLATE, UrlHelper.deriveBaseUrl(url), site);
        this.demandwareResponseParser = demandwareResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);
        String atcBody = String.format(ATC_BODY_TEMPLATE, variants.get(idx));
        idx = (idx + 1) % variants.size();
        BasicHttpResponse basicHttpResponse = httpRequestHelper.performPost(basicRequestClient, atcReq, atcBody);
        demandwareResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
        Notifications.send(attachmentCreater);
       /* attachmentCreater.clearAll();
        String patternStr = "<title>(.*)</title>";
        Pattern pattern = Pattern.compile(patternStr);
        String prodIdStr = "id=\"pid\"\\s+value=\"([0-9]*)\"";
        Pattern prodIdPattern = Pattern.compile(prodIdStr);

        String price = "class=\"value\">$([0-9.]*).</span>";
        Pattern pricePattern = Pattern.compile(price);

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
                isFirstRun = false;
            }
        }

        if(isFirstRun){
            return true;
        }


        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //notify slack
        }

        try{
            List<Header> headerList = new ArrayList<>();
            headerList.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded"));
            logger.info(url + ":" + atcLink + ":" + atcReq);

            BasicHttpResponse basicHttpResponse = httpRequestHelper.performPost(httpClient, config, null, atcLink, headerList, atcReq);
            if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {
                return notify;
            }

            if(basicHttpResponse.getBody().contains("<div class=\"price-adjusted-total\">")){
                logger.info("in stock");

                if(notify){
                    attachmentCreater.addMessages(url, name, "Demandware (" + siteName + ")", null, null);
                   // Notifications.send(discordWebhook, slackObj, attachmentCreater);
                }
                return false;

            } else if(basicHttpResponse.getBody().contains("shoppingBagEmpty")){
                logger.info("oos");
                return true;
            }
            else if(basicHttpResponse.getResponseCode() >= 400){
                return notify;
            } else {
                logger.info("broken");
                return notify;
            }


        } catch(Exception e){
            logger.error(e);
            e.printStackTrace();
            return notify;
        }*/
    }

    public String getUrl(){
        return url;
    }
}
