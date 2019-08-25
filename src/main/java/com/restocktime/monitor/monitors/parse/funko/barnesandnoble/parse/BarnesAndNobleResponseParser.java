package com.restocktime.monitor.monitors.parse.funko.barnesandnoble.parse;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.funko.barnesandnoble.attachment.BarnesandNobleBuilder;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BarnesAndNobleResponseParser {
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private String sku;
    private final String inStock = "<input type=\"submit\" value=\"ADD TO CART\"";
    private final String notAvail = "This item is not available.";
    private final Pattern titlePattern = Pattern.compile("<meta property=\"og:title\" content=\"([^<]*)\"/>");
    private final Pattern imgPattern = Pattern.compile("<meta property=\"og:image\" content=\"([^\"]*)\"/>");
    private final Pattern pricePattern = Pattern.compile("<div priceType=\"Sale\" price=\"([^<]*)\" class=\"");
    private final Pattern statusPattern = Pattern.compile("instock\":\"([^<]*)\"},\"reviewrating");
    private final Pattern pidPattern = Pattern.compile("\"identifier\":\"prd([^<]*)\",\"_name\"");
    private List<String> formatNames;

    public BarnesAndNobleResponseParser(StockTracker stockTracker, String sku, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.sku = sku;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, BasicHttpResponse prodHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        try {

            String apiresponseString = basicHttpResponse.getBody().get();
            Matcher statusMatcher = statusPattern.matcher(apiresponseString);
            Matcher pidMatcher = pidPattern.matcher(apiresponseString);
            String pid = pidMatcher.find()?pidMatcher.group(1):"";
            String statusString = statusMatcher.find()?statusMatcher.group(1):"";

            if(pid.equals(sku) && statusString.equals("true")){
                String responseString = prodHttpResponse.getBody().get();
                Matcher priceMatcher = pricePattern.matcher(responseString);
                Matcher imgMatcher = imgPattern.matcher(responseString);
                String price = priceMatcher.find()?priceMatcher.group(1):"N/A";
                String imgUrl = imgMatcher.find()?imgMatcher.group(1):null;
                Matcher titleMatcher = titlePattern.matcher(responseString);
                String name = titleMatcher.find()?titleMatcher.group(1):"N/A";

                String result = name.replaceAll("[-+.^:!)(,]","");
                result = result.toLowerCase();
                String ebay = String.format("https://www.ebay.com/sch/i.html?_nkw=%s&rt=nc&LH_Sold=1&LH_Complete=1", URLEncoder.encode(result, "UTF-8"));
                String stash = String.format("https://stashpedia.com/search?terms=%s", URLEncoder.encode(result, "UTF-8"));

                if(stockTracker.notifyForObject(sku, isFirst) && keywordSearchHelper.search(name)){

                    BarnesandNobleBuilder.buildAttachments(attachmentCreater, String.format("https://www.barnesandnoble.com/w//123?ean=%s", sku), imgUrl, name, price, ebay, stash, formatNames);
                }
            } else if(statusString.equals("false")){
                stockTracker.setOOS(sku);
            }
        } catch (Exception e){

        }
    }
}
