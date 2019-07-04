package com.restocktime.monitor.monitors.parse.popcultcha.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.monitors.parse.popcultcha.attachment.PopcultchaBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PopCultchaResponseParser  implements AbstractResponseParser {
    private final Pattern productPattern = Pattern.compile("<a class=\"product-image text-center form-group\" href=\"([^\"]*)\"><span class=\"helper\"></span><img title=\"([^\"]*)\" data-src=\"([^\"]*)\"");
    private final Pattern productLinksandTitles = Pattern.compile("<a href=\"([^\"]*)\" title=\"([^\"]*)\" class=\"product-image\">");
    private final Pattern divPattern = Pattern.compile("<ul class=\"products-grid products-grid--max-4-col\">([\\s\\S]*?)<div class=\"toolbar-bottom\">");
    private final Pattern imgPattern = Pattern.compile("<img([\\s\\S]*?)/>");
    private final Pattern imgSrcPattern = Pattern.compile("src=\"([^\"]*)\"");
    private final Pattern pricePattern = Pattern.compile("<span class=\"price\">([^<]*)</span> </span>");

    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private List<String> formatNames;

    public PopCultchaResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {
            return;
        }

        String responseString = basicHttpResponse.getBody().replaceAll("\n", "").replaceAll("\\s+", " ");
//        Matcher productMatcher = productPattern.matcher(responseString);
//        Matcher linkMatcher = productLinksandTitles.matcher(responseString);
        Matcher divMatcher = divPattern.matcher(responseString);
        if (divMatcher.find()) {
            String divString = divMatcher.group(1);
            Matcher imgMatcher = imgPattern.matcher(divString);
            Matcher linkMatcher = productLinksandTitles.matcher(divString);
            Matcher priceMatcher = pricePattern.matcher(divString);
            while (linkMatcher.find() && imgMatcher.find() && priceMatcher.find()) {
                String link = linkMatcher.group(1);
                String name = linkMatcher.group(2);
                String price = priceMatcher.group(1);
                String imgString = imgMatcher.group(1);
                Matcher src = imgSrcPattern.matcher(imgString);
                String imgUrl = src.find() ?  src.group(1) : null;


                if (stockTracker.notifyForObject(link, isFirst) && keywordSearchHelper.search(name)) {
                    PopcultchaBuilder.buildAttachments(attachmentCreater, link, imgUrl, name, price, formatNames);
                }
            }
        }
    }
}
