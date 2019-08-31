package com.restocktime.monitor.monitors.parse.aio.grosbasket;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrosBasketResponseParser implements AbstractResponseParser {
    private static final Pattern IMG_PATTERN = Pattern.compile("property=\"og:image\"\\s+content=\"([^\"]*)\"");
    private static final Pattern TITLE_PATTERN = Pattern.compile("property=\"og:title\"\\s+content=\"([^\"]*)\"");

    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;

    public GrosBasketResponseParser(StockTracker stockTracker, String url, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        if (responseString.contains("productAddToCartForm.submit(this)")) {
            if(stockTracker.notifyForObject(url, isFirst)) {
                String title = "TITLE UNAVAILABLE";
                String img = null;

                Matcher imgMatcher = IMG_PATTERN.matcher(responseString);
                Matcher titleMatcher = TITLE_PATTERN.matcher(responseString);

                if (imgMatcher.find()) {
                    img = imgMatcher.group(1);
                }

                if (titleMatcher.find()) {
                    title = titleMatcher.group(1);
                }

                DefaultBuilder.buildAttachments(attachmentCreater, url, img, "GrosBasket", title, formatNames);
            }
        } else if(responseString.contains("alt=\"Sold Out\"") || responseString.contains("")) {
            stockTracker.setOOS(url);
        }
    }
}
