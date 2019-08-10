package com.restocktime.monitor.monitors.parse.oneblockdown.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.oneblockdown.model.product.Product;
import com.restocktime.monitor.monitors.parse.oneblockdown.model.product.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneBlockDownProductPageResponseParser implements AbstractResponseParser {
    private static final Logger LOGGER = Logger.getLogger(OneBlockDownProductPageResponseParser.class);
    private StockTracker stockTracker;
    private Map<String, String> oosMap;
    private List<String> formatNames;
    private ObjectMapper objectMapper;
    private String url;


    String patternStr = "var preloadedStock = ([^;]*);";
    Pattern pattern = Pattern.compile(patternStr);
    private Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private Pattern imgPattern = Pattern.compile("<link rel=\"image_src\" href=\"([^\"]*)\" />");

    public OneBlockDownProductPageResponseParser(String url, StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        oosMap = new HashMap<>();
        this.formatNames = formatNames;
        this.objectMapper = new ObjectMapper();
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) throws Exception {
        oosMap.clear();
        String responseString = basicHttpResponse.getBody().get();

        Matcher matcher = pattern.matcher(responseString);
        if(matcher.find()){
            String jsonStr = "{\"variants\":" + matcher.group(1) + "}";
            Product product =
                    objectMapper.readValue(jsonStr, Product.class);
            boolean inStock = false;
            for(Variant variant : product.getVariants()) {
                if(variant.getIsInStock() && stockTracker.notifyForObject(url, isFirst)){
                    Matcher nameMatcher = titlePattern.matcher(responseString);
                    String name = "Hype thing";
                    if (nameMatcher.find()) {
                        name = nameMatcher.group(1);
                    }

                    String img = null;
                    Matcher imgMatcher = imgPattern.matcher(responseString);
                    if (imgMatcher.find()) {
                        img = imgMatcher.group(1);
                    }

                    DefaultBuilder.buildAttachments(attachmentCreater, url, img, "OBD", name, formatNames);
                    break;
                }
            }

            if (product.getVariants().isEmpty()){
                stockTracker.setOOS(url);
            }

        }

    }
}
