package com.restocktime.monitor.monitors.parse.shopify.parse;

import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.LinkCheckStarter;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.ShopifyLinkChecker;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopifyKwAbstractResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private boolean hitUnblockedSitemap;
    private String storeName;
    private LinkCheckStarter linkCheckStarter;
    private boolean scanUnknown;
    private List<String> formatNames;

    private final int MAX_COUNT = 100;
    private int count;
    private String url;


    final static Logger logger = Logger.getLogger(ShopifyKwAbstractResponseParser.class);
    private final Pattern productPattern = Pattern.compile("<url><loc>([^<]*)</loc><lastmod>[^<]*</lastmod><changefreq>[^<]*</changefreq><image:image><image:loc>([^<]*)</image:loc><image:title>([^<]*)</image:title></image:image></url>");

    //1 = url, 2 = image, 3 = title
    public ShopifyKwAbstractResponseParser(String url, StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, boolean hitUnblockedSitemap, String storeName, LinkCheckStarter linkCheckStarter, boolean scanUnknown, AttachmentCreater attachmentCreater, List<String> formatNames) {
        this.url = url;
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.hitUnblockedSitemap = false;
        this.storeName = storeName;
        this.linkCheckStarter = linkCheckStarter;
        this.scanUnknown = scanUnknown;
        this.formatNames = formatNames;

        count = 0;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        if(basicHttpResponse.getResponseCode().get() == 430){
            logger.info("Banned asf shopify");
            return;
        }

        if(
                (basicHttpResponse.getResponseCode().get() == 403 || basicHttpResponse.getResponseCode().get() == 404)
                        && !basicHttpResponse.getBody().get().contains("Squid")
        ) {
            if(hitUnblockedSitemap) {
                hitUnblockedSitemap = false;
                DefaultBuilder.buildAttachments(attachmentCreater, UrlHelper.deriveBaseUrl(url), null, UrlHelper.getHost(url), "SITEMAP BLOCKED", formatNames);
            }
            return;
        }

        String patternStr = "<image:title>(.*)</image:title>";
        Pattern pattern = Pattern.compile(patternStr);
        String linkStr = "<loc>(.*)</loc>";
        String photoLink = "<image:loc>(.*)</image:loc>";
        Pattern photoLinkPatter = Pattern.compile(photoLink);
        Pattern linkPattern = Pattern.compile(linkStr);

        List<String> knownLinks = new ArrayList<>();
        List<String> unknownLinks = new ArrayList<>();

        String responseString = basicHttpResponse.getBody().get().replaceAll("\n", "").replaceAll(">\\s+<", "><");
        Matcher productMatcher = productPattern.matcher(responseString);
        while(productMatcher.find()){
            hitUnblockedSitemap = true;
            count = count > 10 ? count : count++;
            String url = productMatcher.group(1);
            String img = productMatcher.group(2);
            String name = productMatcher.group(3);
            if(!stockTracker.notifyForObject(url, isFirst)){
                continue;
            }

            if(keywordSearchHelper.search(name + " " + img)) {
                knownLinks.add(url);
            } else if(scanUnknown) {
                unknownLinks.add(url);
            }
        }

        List<ShopifyLinkChecker> shopifyLinkCheckers = new ArrayList<>();
        if(knownLinks.size() > 0 && count > 10){
            shopifyLinkCheckers.addAll(linkCheckStarter.generateLinkCheckStarters(knownLinks, storeName, UrlHelper.deriveBaseUrl(knownLinks.get(0)), new KeywordSearchHelper(keywordSearchHelper), new AttachmentCreater(attachmentCreater), new HttpRequestHelper(), new ArrayList<>(formatNames), true));
        }

        if(unknownLinks.size() > 0 && count > 10){
            logger.info(unknownLinks.size());
            shopifyLinkCheckers.addAll(linkCheckStarter.generateLinkCheckStarters(unknownLinks, storeName, UrlHelper.deriveBaseUrl(unknownLinks.get(0)), new KeywordSearchHelper(keywordSearchHelper), new AttachmentCreater(attachmentCreater), new HttpRequestHelper(), new ArrayList<>(formatNames), false));
        }


        if(shopifyLinkCheckers.size() > 0){
            linkCheckStarter.runChecker(shopifyLinkCheckers);
        }
    }

    public void updateKeywords(Map<String, String> keywords){
        keywordSearchHelper.updateKeywords(keywords);
    }

}
