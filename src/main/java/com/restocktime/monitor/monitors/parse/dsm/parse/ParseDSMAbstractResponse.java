package com.restocktime.monitor.monitors.parse.dsm.parse;

import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.password.PasswordHelper;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.LinkCheckStarter;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.ShopifyLinkChecker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseDSMAbstractResponse implements AbstractResponseParser {


    private String url;
    private StockTracker stockTracker;
    private String region;
    private final String productUrlPatternStr = "href=\"([/A-Za-z0-9-]*/product.+)\"";
    private final String productName = "grid-view-item__title\">(.*)</div>";

    private Pattern urlPattern;
    private Pattern namePattern;
    private LinkCheckStarter linkCheckStarter;
    private List<String> formatNames;
    private KeywordSearchHelper keywordSearchHelper;
    private boolean isPassUp;


    public ParseDSMAbstractResponse(StockTracker stockTracker, String url, String region, LinkCheckStarter linkCheckStarter, KeywordSearchHelper keywordSearchHelper, List<String> formatNames){
        this.url = url;
        this.region = region;
        this.stockTracker = stockTracker;
        this.urlPattern = Pattern.compile(productUrlPatternStr);
        this.namePattern = Pattern.compile(productName);
        this.formatNames = formatNames;
        this.linkCheckStarter = linkCheckStarter;
        this.keywordSearchHelper = keywordSearchHelper;
        this.isPassUp = false;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String s = basicHttpResponse.getBody().get();

        isPassUp = PasswordHelper.getPassStatus(attachmentCreater, basicHttpResponse, url, isFirst, isPassUp, formatNames);


        Matcher m = urlPattern.matcher(basicHttpResponse.getBody().get());
        Matcher name = namePattern.matcher(s);

        List<String> knownLinks = new ArrayList<>();

        while(m.find()){
            String path = m.group(1).replaceAll("/collections/[^/]+/", "/");
            String productUrl = url + path;
            if(stockTracker.notifyForObject(productUrl, isFirst) && !knownLinks.contains(productUrl)) {
                knownLinks.add(productUrl);
            }
        }

        List<ShopifyLinkChecker> shopifyLinkCheckers = new ArrayList<>();
        if(knownLinks.size() > 0){
            shopifyLinkCheckers.addAll(linkCheckStarter.generateLinkCheckStarters(knownLinks, UrlHelper.getHost(url), UrlHelper.deriveBaseUrl(knownLinks.get(0)), new KeywordSearchHelper(keywordSearchHelper), attachmentCreater, new HttpRequestHelper(), new ArrayList<>(formatNames), true));
        }

        if(shopifyLinkCheckers.size() > 0){
            linkCheckStarter.runChecker(shopifyLinkCheckers);
        }
    }
}
