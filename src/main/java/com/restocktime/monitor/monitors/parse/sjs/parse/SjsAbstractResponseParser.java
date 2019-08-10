package com.restocktime.monitor.monitors.parse.sjs.parse;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SjsAbstractResponseParser implements AbstractResponseParser {

    private StockTracker stockTracker;
    String patternStr = "<a class=\"product_img_link\" href=\"([^\"]*)\" title=\"([^\"]*)\"";
    Pattern pattern;
    private List<String> formatNames;

    public SjsAbstractResponseParser(StockTracker stockTracker, Pattern pattern, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.pattern = pattern;
        this.pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;

    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }
        String responseString = basicHttpResponse.getBody().get();
        responseString = responseString.replaceAll(">\\s+<", "><");
        Matcher m = pattern.matcher(responseString);
        while(m.find()) {
            if (stockTracker.notifyForObject(m.group(1), isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, m.group(1), null, "SJS", m.group(2), formatNames);
            }
        }
    }

}
