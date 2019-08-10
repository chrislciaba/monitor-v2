package com.restocktime.monitor.monitors.parse.offwhite.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OffWhiteSoldOutTagResponseParser {
    final static Logger logger = Logger.getLogger(OffWhiteProductAbstractResponseParser.class);

    private StockTracker stockTracker;
    private final Pattern productPattern = Pattern.compile("<article class='product' data-json-url='[^']*' id='[^']*' itemscope='' itemtype='http://schema\\.org/Product'>[^\n]+<div class='size-box js-size-box'>");
    private final Pattern linkPattern =Pattern.compile( "<a itemProp=\"url\" href=\"([^\"]*)\"><span content='([^\']*)' itemProp='name' style='display:none'></span>");
    private final Pattern titlePattern = Pattern.compile("<div class='brand-name'>([^<]*)</div>");
    private final String SOLD_OUT_PATTERN = "availability not_on_sale";
    private List<String> formatNames;
    private List<String> skuList;

    public OffWhiteSoldOutTagResponseParser(String skus, StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames =formatNames;
        this.skuList = Arrays.asList(skus.split(";"));

    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll("\\s+", " ").replaceAll(">\\s+<", "><");
        List<String> productStrList = new ArrayList<>();

        String[] splitList = responseString.split("<div class='sizes'>");
        for(String s : splitList){
            Matcher productMatcher = productPattern.matcher(s);
            if(productMatcher.find()) {
                String productStr = productMatcher.group(0);
                for (String sku : skuList) {
                    if (productStr.contains(sku)) {
                        productStrList.add(productStr);
                    }
                }
            }
        }

        for(String htmlProduct : productStrList){

            Matcher linkMatcher = linkPattern.matcher(htmlProduct);

            if(linkMatcher.find()){
                String href = linkMatcher.group(1);
                String tag = linkMatcher.group(2);

                if(htmlProduct.contains(SOLD_OUT_PATTERN)){
                    logger.info("OOS");
                    stockTracker.setOOS(tag);
                    continue;
                }

                if(stockTracker.notifyForObject(tag, isFirst)){
                    Matcher nameMatcher = titlePattern.matcher(htmlProduct);
                    String name = nameMatcher.find()?nameMatcher.group(1):"N/A";
                    DefaultBuilder.buildAttachments(attachmentCreater, UrlHelper.urlRandNumberAppended("https://www.off---white.com" + href), null, "OW", name, formatNames);
                }
            }
        }
    }
}
