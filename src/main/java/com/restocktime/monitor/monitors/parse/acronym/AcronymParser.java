package com.restocktime.monitor.monitors.parse.acronym;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcronymParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private String baseUrl;
    private final String PATTERN_STR = "<a class=\"tile[^>]*href=\"([^\"]*)\"><div class=\"name\">[^\"]*</div><img src=\"([^\"]*)\" alt=\"[^\"]*\" /><div class=\"product-info\"><div class=\"\"><img src=\"[^\"]*\" alt=\"[^\"]*\" /><h2>[^<]*</h2></div><div class=\"txt\"><div>([^<]*)</div>";
    private Pattern pattern;
    private List<String> formatNames;

    public AcronymParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, String baseUrl, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.baseUrl = baseUrl;
        this.pattern = Pattern.compile(PATTERN_STR);
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        String responseString = basicHttpResponse.getBody();
        responseString = responseString.replaceAll(">\\s+<", "><");
        Matcher m = pattern.matcher(responseString);
        while(m.find()) {
            if (keywordSearchHelper.search(m.group(3))) {
                if(stockTracker.notifyForObject(m.group(1), isFirst)) {
                    DefaultBuilder.buildAttachments(attachmentCreater, baseUrl + m.group(1), baseUrl + m.group(2), "Acronym", m.group(3), formatNames);
                }
            }
        }


    }
}
