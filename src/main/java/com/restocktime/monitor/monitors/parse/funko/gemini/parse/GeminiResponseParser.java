package com.restocktime.monitor.monitors.parse.funko.gemini.parse;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiResponseParser  implements AbstractResponseParser {
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private String url;
    private Pattern pattern = Pattern.compile("<div class=\"ProductImage QuickView\" data-product=\"[^\"]*\"><a href=\"([^\"]*)\"><img src=\"([^\"]*)\" alt=\"([^\"]*)\" />");
    private List<String> formatNames;

    public GeminiResponseParser(StockTracker stockTracker, String url, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");

        Matcher productMatcher = pattern.matcher(responseString);
        while(productMatcher.find()){
            String url = productMatcher.group(1);
            String imgUrl = productMatcher.group(2);
            String name = productMatcher.group(3);

            if(stockTracker.notifyForObject(url, isFirst) && keywordSearchHelper.search(name)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, imgUrl, "Gemini", name, formatNames);

            }
        }
    }
}
