package com.restocktime.monitor.monitors.parse.shopify.parse;

import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.LinkCheckStarter;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.ShopifyLinkChecker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopifyAtomResponseParser implements AbstractResponseParser {

    private Pattern pattern = Pattern.compile("<link rel=\"alternate\" type=\"text/html\" href=\"([^\"]*)\"/><title>([^<]*)</title>");

    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private boolean hitUnblockedSitemap;
    private String storeName;
    private LinkCheckStarter linkCheckStarter;
    private boolean scanUnknown;
    private List<String> formatNames;
    private String url;

    private final int MAX_COUNT = 100;
    private int count;
    private int errors;
    private int bans;

    final static Logger logger = Logger.getLogger(ShopifyAtomResponseParser.class);


    public ShopifyAtomResponseParser(String url, StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, boolean hitUnblockedSitemap, String storeName,  LinkCheckStarter linkCheckStarter, boolean scanUnknown, List<String> formatNames) {
        this.url = url;
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.hitUnblockedSitemap = hitUnblockedSitemap;
        this.storeName = storeName;
        this.linkCheckStarter = linkCheckStarter;
        this.scanUnknown = scanUnknown;
        this.formatNames = formatNames;

        count = 0;
        errors = 0;
        bans = 0;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(count == MAX_COUNT){
            count = 0;
            bans = 0;
            errors = 0;
        }


        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        if(basicHttpResponse.getResponseCode().get() == 430){
            logger.info("Banned asf shopify");
            return;
        }

        List<List<String>> linkKws = new ArrayList<>();
        List<String> knownLinks = new ArrayList<>();
        List<String> unknownLinks = new ArrayList<>();

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");
        Matcher m = pattern.matcher(responseString);

        while(m.find()){
            String link = m.group(1);
            String keywordString = m.group(2);


            if(!stockTracker.notifyForObject(link, isFirst)){
                continue;
            }

            if(keywordSearchHelper.search(keywordString)) {
                knownLinks.add(link);
            } else if(scanUnknown) {
                unknownLinks.add(link);
            }
        }

        List<ShopifyLinkChecker> shopifyLinkCheckers = new ArrayList<>();
        if(knownLinks.size() > 0){
            shopifyLinkCheckers.addAll(linkCheckStarter.generateLinkCheckStarters(knownLinks, storeName, UrlHelper.deriveBaseUrl(knownLinks.get(0)), new KeywordSearchHelper(keywordSearchHelper), new AttachmentCreater(attachmentCreater), new HttpRequestHelper(), formatNames, true));
        }

        if(unknownLinks.size() > 0){
            logger.info(unknownLinks.size());
            shopifyLinkCheckers.addAll(linkCheckStarter.generateLinkCheckStarters(unknownLinks, storeName, UrlHelper.deriveBaseUrl(unknownLinks.get(0)), new KeywordSearchHelper(keywordSearchHelper), new AttachmentCreater(attachmentCreater), new HttpRequestHelper(), formatNames, false));
        }


        if(shopifyLinkCheckers.size() > 0){
            linkCheckStarter.runChecker(shopifyLinkCheckers);
        }
    }
}
