package com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.ArrayList;
import java.util.List;

public class LinkCheckStarter {
    private List<BasicRequestClient> basicRequestClient;

    public LinkCheckStarter(List<BasicRequestClient> basicRequestClient){
        this.basicRequestClient = basicRequestClient;
    }

    public void runChecker(List<ShopifyLinkChecker> shopifyLinkCheckers){
        //ExecutorService executor = Executors.newFixedThreadPool(basicRequestClient.size());
        for (ShopifyLinkChecker shopifyLinkChecker : shopifyLinkCheckers){
            shopifyLinkChecker.run();
          //  executor.execute(shopifyLinkChecker);
        }
       // executor.shutdown();
      //  while (!executor.isTerminated()) {   }

    }

    public List<ShopifyLinkChecker> generateLinkCheckStarters(
            List<String> links,
            String storeName,
            String storeBaseUrl,
            KeywordSearchHelper keywordSearchHelper,
            AttachmentCreater attachmentCreater,
            HttpRequestHelper httpRequestHelper,
            List<String> formatNames,
            boolean isKnownLink){
        List<ShopifyLinkChecker> shopifyLinkCheckers  = new ArrayList<>();
        int idx = 0;

        for(String link : links){
            shopifyLinkCheckers.add(new ShopifyLinkChecker(link, storeName, storeBaseUrl, basicRequestClient.get(idx), keywordSearchHelper, new AttachmentCreater(attachmentCreater), httpRequestHelper, isKnownLink, formatNames));
            idx = (idx + 1) % basicRequestClient.size();
        }
        return shopifyLinkCheckers;
    }
}
