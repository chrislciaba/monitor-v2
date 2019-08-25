package com.restocktime.monitor.monitors.parse.aio.patta;

import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PattaAbstractResponseParser implements AbstractResponseParser {
    String patternStr = "<a\\s+class=\"product-item-link\"\\s+href=\"([^\"]*)\">([^>]*)</a>";
    Pattern pattern;
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;

    public PattaAbstractResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        responseString = responseString.replaceAll(">\\s+<", "><");
        Matcher m = pattern.matcher(responseString);
        while(m.find()) {

            String l = m.group(1);
            String name = m.group(2).replaceAll("\\s+", " ").trim();
            if (keywordSearchHelper.search(name) && stockTracker.notifyForObject(l, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, l, null, "Patta", name, formatNames);
            }
        }
    }
}
