package com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.ShopifyJson;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.monitors.parse.shopify.attachment.ShopifyBuilder.buildAttachments;

public class ShopifyLinkChecker {
    private String link;
    private ObjectMapper objectMapper;
    private String storeName;
    private KeywordSearchHelper keywordSearchHelper;
    private Random r;
    private HttpRequestHelper httpRequestHelper;
    private boolean isKnownLink;
    private BasicRequestClient basicRequestClient;
    private AttachmentCreater attachmentCreater;
    private List<String> formatNames;
    private final Pattern jsonPatternDsm = Pattern.compile("<script type=\"application/json\" id=\"ProductJson-product-template\">\\s*(.*)\\s*</script>");
    private final Pattern dsmTokenPattern = Pattern.compile("val\\('([0-9]*)'\\)");


    final static Logger logger = Logger.getLogger(ShopifyLinkChecker.class);


    public ShopifyLinkChecker(String link,
                              String storeName,
                              String storeBaseUrl,
                              BasicRequestClient basicRequestClient,
                              KeywordSearchHelper keywordSearchHelper,
                              AttachmentCreater attachmentCreater,
                              HttpRequestHelper httpRequestHelper,
                              boolean isKnownLink,
                              List<String> formatNames) {

        this.basicRequestClient = basicRequestClient;
        this.storeName = storeName;
        this.link = link;
        this.r = new Random();
        this.keywordSearchHelper = keywordSearchHelper;
        this.objectMapper = new ObjectMapper();
        this.httpRequestHelper = httpRequestHelper;
        this.isKnownLink = isKnownLink;
        this.attachmentCreater = attachmentCreater;
        this.formatNames = formatNames;
    }

    public void run(){
        String responseString = "";
        String dsmToken = null;
        BasicHttpResponse basicHttpResponse;

        if(link.contains("eflash")){
            basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, link + "?limit=" + Long.toString(Math.abs(r.nextLong())) + Long.toString(Math.abs(r.nextLong())));
            if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
                return;
            }
            Matcher m = jsonPatternDsm.matcher(basicHttpResponse.getBody());
            if(m.find()){
                responseString = m.group(1).trim();
            }
            Matcher tokMatcher = dsmTokenPattern.matcher(basicHttpResponse.getBody());
            if(tokMatcher.find()){
                dsmToken = tokMatcher.group(1);
            }
        } else {

            basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, link + "?format=js&limit=" + Long.toString(Math.abs(r.nextLong())) + Long.toString(Math.abs(r.nextLong())));
            if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
                return;
            }
            responseString = basicHttpResponse.getBody();
        }


        try {
            ShopifyJson shopifyJson = objectMapper.readValue(responseString, ShopifyJson.class);
            String searchStr = String.join(" ", shopifyJson.getTags()) + " " +
                    "https:" + shopifyJson.getFeatured_image() + " " +
                    shopifyJson.getTitle();

            if(
                    isKnownLink ||
                    keywordSearchHelper.search(searchStr) ||
                    kithCondition(link, shopifyJson.getTitle()) ||
                    bdgaCondition(link, shopifyJson.getTags())){
                List<Variant> variants = new ArrayList<>();
                for (Variant variant : shopifyJson.getVariants()) {
                    if (variant.getAvailable()) {
                        variants.add(variant);
                    }
                }


                if(!variants.isEmpty()) {
                    buildAttachments(
                            attachmentCreater,
                            link,
                            "https:" + shopifyJson.getFeatured_image(),
                            shopifyJson.getTitle(),
                            Integer.toString(shopifyJson.getPrice() / 100),
                            variants,
                            "",
                            formatNames,
                            3,
                            dsmToken == null ? Optional.empty() : Optional.of("_HASH"),
                            dsmToken == null ? Optional.empty() : Optional.of(dsmToken)
                    );

                }
            } else {
                    logger.info("found but oos");
            }
        } catch(Exception e){

        }

        Notifications.send(attachmentCreater);
    }

    private boolean kithCondition(String url, String title){
        return url.contains("kith.com") && title.equals(title.toUpperCase());
    }

    private boolean bdgaCondition(String url, List<String> tags){
        return url.contains("kith.com") && tags.contains("human");
    }


}
