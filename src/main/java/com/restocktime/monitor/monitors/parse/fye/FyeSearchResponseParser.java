package com.restocktime.monitor.monitors.parse.fye;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FyeSearchResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private String url;
    private Pattern pattern = Pattern.compile("<a class=\"c-product-tile__image c-produc-tile__link thumb-link\" href=\"([^\"]*)\"><img class=\"c-product-tile__image-src js-product-tile-main-image\" src=\"([^\"]*)\" alt=\"([^\"]*)\"");
    private List<String> formatNames;

    public FyeSearchResponseParser(StockTracker stockTracker, String url, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><").replaceAll("\\s+", " ");

        Matcher productMatcher = pattern.matcher(responseString);
        while(productMatcher.find()){
            String handle = productMatcher.group(1);
            String imgUrl = productMatcher.group(2);
            String name = productMatcher.group(3);

            if(stockTracker.notifyForObject(handle, isFirst) && keywordSearchHelper.search(name)){
                DefaultBuilder.buildAttachments(attachmentCreater, String.format("https://www.fye.com/%s", handle), imgUrl, "FYE", name, formatNames);
            }
        }
    }
}
