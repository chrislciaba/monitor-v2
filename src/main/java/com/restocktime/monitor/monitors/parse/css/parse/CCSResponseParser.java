package com.restocktime.monitor.monitors.parse.css.parse;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CCSResponseParser {
    //<a itemprop="url" content="https://shop.ccs.com/nike-sb-dunk-low-shoes-dark-grey-black-white-6-0" href="https://shop.ccs.com/nike-sb-dunk-low-shoes-dark-grey-black-white-6-0" title="Nike SB Dunk Low Pro Shoes - Dark Grey/Black/White" class="product-image">

    final static Logger logger = Logger.getLogger(CCSResponseParser.class);

    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private Pattern pattern = Pattern.compile("<a href=\"([^\"]*)\" title=\"([^\"]*)\">");

    public CCSResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
    }
    //<label for="variant_id_107700">M</label>


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        Matcher productMatcher = pattern.matcher(responseString);
        boolean found = false;
        logger.info(responseString);
        while(productMatcher.find()){
            found = true;
            String url = productMatcher.group(1);
            String name = productMatcher.group(2);

            if(keywordSearchHelper.search(name) && stockTracker.notifyForObject(name, false)){
               // attachmentCreater.addMessages(url, name, "CCS", null, null);
            }
        }

        if(!found){
            logger.info("N/A");
        }
    }
}
