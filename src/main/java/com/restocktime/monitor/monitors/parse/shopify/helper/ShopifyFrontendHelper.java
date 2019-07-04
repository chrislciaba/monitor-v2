package com.restocktime.monitor.monitors.parse.shopify.helper;

import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.LinkCheckStarter;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.ShopifyLinkChecker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopifyFrontendHelper {

    private Set<String> traversedLinks;
    private String baseUrl;



    final static Logger logger = Logger.getLogger(ShopifyFrontendHelper.class);
    private final Pattern productPattern = Pattern.compile("/products/[^\"?]+");

    public ShopifyFrontendHelper(String url) {
        this.traversedLinks = new HashSet<>();
        this.baseUrl = UrlHelper.deriveBaseUrl(url);
    }

    public List<String> findLinks(BasicHttpResponse basicHttpResponse, boolean isFirst){
        List<String> linkList = new ArrayList<>();
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return linkList;
        }

        if(basicHttpResponse.getResponseCode() == 430 || basicHttpResponse.getResponseCode() == 429){
            logger.info("Banned asf shopify");
            return linkList;
        }

        if(
                (basicHttpResponse.getResponseCode() == 403 || basicHttpResponse.getResponseCode() == 404)
                        && !basicHttpResponse.getBody().contains("Squid")

        ){
            return linkList;
        }


        String responseString = basicHttpResponse.getBody();
        Matcher productMatcher = productPattern.matcher(responseString);
        Set<String> currentLinks = new HashSet<>();
        while (productMatcher.find()) {
            String link = baseUrl + productMatcher.group(0);
            if (!traversedLinks.contains(link) &&  !link.contains(".jpeg") && !link.contains(".jpg") && !link.contains("{{handle}}")) {
                currentLinks.add(link);
            }
        }

        if (!isFirst) {
            for (String s : currentLinks) {
                linkList.add(s);
                logger.info(s);
            }
        }

        traversedLinks.addAll(currentLinks);

        return linkList;
    }
}
