package com.restocktime.monitor.monitors.parse.walmart.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.walmart.model.terra.TerraResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.monitors.parse.walmart.attachment.WalmartTerraBuilder;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalmartTerraResponseParser implements AbstractResponseParser {
    private final Pattern imagePattern = Pattern.compile("itemprop=\"image\" src=\"([^\"]*)\"");
    private final Pattern pricePattern = Pattern.compile("itemprop=\"price\" content=\"([^<]*)\"");

    final static Logger logger = Logger.getLogger(WalmartResponseParser.class);

    private ObjectMapper objectMapper;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private String pid;

    public WalmartTerraResponseParser(ObjectMapper objectMapper, StockTracker stockTracker, List<String> formatNames, String pid) {
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.pid = pid;
    }

    public void parse(BasicHttpResponse basicHttpResponse,/* BasicHttpResponse productHttpResponse,*/ AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        try {
            String responseString = "";//productHttpResponse.getBody();

            Matcher picMatcher = imagePattern.matcher(responseString);
            Matcher priceMatcher = pricePattern.matcher(responseString);
            String pic = "";
            String price = "N/A";
            if(picMatcher.find()){
                pic = picMatcher.group(1);
            }
            if(priceMatcher.find()){
                price = priceMatcher.group(1);
            }

            TerraResponse terraResponse = objectMapper.readValue(basicHttpResponse.getBody().get(), TerraResponse.class);

            if(terraResponse.getPayload() != null && terraResponse.getPayload().getOffers() != null) {
                for (String k : terraResponse.getPayload().getOffers().keySet()) {
                    if (terraResponse.getPayload().getOffers().get(k) != null && terraResponse.getPayload().getOffers().get(k).getSellerId() !=null) {
                        String seller = terraResponse.getPayload().getOffers().get(k).getSellerId();
                        if(seller.equals("F55CDC31AB754BB68FE0B39041159D63")) {
                            String status = terraResponse.getPayload().getOffers().get(k).getProductAvailability().getAvailabilityStatus();
                            if (status.equals("IN_STOCK")) {
                                if (stockTracker.notifyForObject(pid, isFirst)) {
                                    String name = "N/A";
                                    for (String k1 : terraResponse.getPayload().getProducts().keySet()) {
                                        name = terraResponse.getPayload().getProducts().get(k1).getProductAttributes().getProductName();
                                        break;
                                    }
                                    String result = name.replaceAll("[-+.^:!)(,]","");
                                    result = result.toLowerCase();
                                    String ebay = String.format("https://www.ebay.com/sch/i.html?_nkw=%s&rt=nc&LH_Sold=1&LH_Complete=1", URLEncoder.encode(result, "UTF-8"));
                                    String stash = String.format("https://stashpedia.com/search?terms=%s", URLEncoder.encode(result, "UTF-8"));
                                    logger.info("IN STOCK - " + name);
                                    WalmartTerraBuilder.buildAttachments(attachmentCreater, String.format("https://www.walmart.com/ip/%s", pid), pic, name, price, String.format("https://affil.walmart.com/cart/addToCart?items=%s", pid), ebay, stash, formatNames);
                                }
                            } else {
                                stockTracker.setOOS(pid);
                                logger.info("OOS - " + pid);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){

        }
    }
}
