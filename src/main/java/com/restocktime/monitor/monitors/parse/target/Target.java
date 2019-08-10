package com.restocktime.monitor.monitors.parse.target;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;

import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.supreme.parse.SupremeProductParseAbstractResponse;
import com.restocktime.monitor.monitors.parse.target.parse.TargetAbstractResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Target extends AbstractMonitor {
    final static Logger logger = Logger.getLogger(SupremeProductParseAbstractResponse.class);
    private String url;
    private int delay;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private TargetAbstractResponseParser targetResponseParser;
    private final String redsky = "https://redsky.target.com/v2/pdp/%s/%s?excludes=promotion,taxonomy,bulk_ship,awesome_shop,question_answer_statistics,rating_and_review_reviews,rating_and_review_statistics,deep_red_labels";

    public Target(String sku, int delay, AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, TargetAbstractResponseParser targetResponseParser){
        if(sku.contains("323-01")){
            this.url = String.format(redsky, "dpci", sku);
        }
        else {
            this.url = String.format(redsky, "tcin", sku);
        }
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.targetResponseParser = targetResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);
        
        try {
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            targetResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);

        }

    }

    public String getUrl(){
        return url;
    }
}
