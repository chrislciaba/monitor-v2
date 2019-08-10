package com.restocktime.monitor.monitors.parse.demandware.parse;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.demandware.attachment.DemandwareBuilder;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteAtcResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class DemandwareGetResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private final Pattern imagePattern = Pattern.compile("<img itemprop=\"image\" class=\"primary-image\" src=\"([^\"]*)\" alt=\"[^\"]*\" title=\"([^\"]*)\"/>");
    private final Pattern pricePattern = Pattern.compile("<span class=\"price-standard\" title=\"Sale Price\">([^<]*)</span>");
    private final Pattern stockPattern = Pattern.compile("\"ats\":\"([0-9]*)\"");
    private String url;
    private String sku;
    private List<String> formatNames;

    private final String fyeOOS = "<span class=\"u-text-red not-available-msg\">";
    private final String fyeInStock = "<span class=\"in-stock-msg\">";
    private final Pattern fyeTitlePattern = Pattern.compile("itemprop=\"name\">([^<]*)</h1>");
    private final Pattern fyePricePattern = Pattern.compile("price: \"([^\"]*)\",");
    private final Pattern fyeImgPattern = Pattern.compile("image_url: \"([^\"]*)\",");


    final static Logger logger = Logger.getLogger(DemandwareGetResponseParser.class);


    public DemandwareGetResponseParser(StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse/*, BasicHttpResponse stockHttpResponse*/, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        String stockString = "";//stockHttpResponse.getBody();

        //Matcher qty = cartItemPattern.matcher(responseString);
        Matcher titleAndPic = imagePattern.matcher(responseString);
        Matcher priceMatcher = pricePattern.matcher(responseString);
        Matcher stockQty = stockPattern.matcher(stockString);
        if(stockQty.find()){
            String qtyStr = stockQty.group(1);
            Integer quantity = Integer.parseInt(qtyStr);
            if (qtyStr.equals("0")){
                stockTracker.setOOS(url);
                logger.info("OOS - " + url);
            } else if(quantity > 0 && stockTracker.notifyForObject(url, isFirst)){
                String title = "N/A";
                String pic = "";
                String price = "N/A";


                if(responseString.contains("hottopic.com") || responseString.contains("boxlunch.com")) {
                    if (titleAndPic.find()) {
                        title = titleAndPic.group(2);
                        pic = titleAndPic.group(1);
                    }

                    if (priceMatcher.find()) {
                        price = priceMatcher.group(1);
                    }


                    try {
                        String result = title.replaceAll("[-+.^:!)(,]", "");
                        result = result.toLowerCase();
                        String ebay = String.format("https://www.ebay.com/sch/i.html?_nkw=%s&rt=nc&LH_Sold=1&LH_Complete=1", URLEncoder.encode(result, "UTF-8"));
                        String stash = String.format("https://stashpedia.com/search?terms=%s", URLEncoder.encode(result, "UTF-8"));

                        logger.info("IN STOCK");

                        //DemandwareHTBLBuilder.buildAttachments(attachmentCreater, url, pic, title, price, qtyStr, atcFG, atcPR, ebay, stash, formatNames);
                    } catch (Exception e){
                        logger.error(EXCEPTION_LOG_MESSAGE, e);
                    }

                } else if(responseString.contains("fye.com")){
                    Matcher fyeTitle = fyeTitlePattern.matcher(responseString);
                    Matcher fyePrice = fyePricePattern.matcher(responseString);
                    Matcher fyeImg = fyeImgPattern.matcher(responseString);
                    if(fyeTitle.find()){
                        title = fyeTitle.group(1);
                    }

                    if(fyePrice.find()){
                        price = String.format("$%s",fyePrice.group(1));
                    }

                    if(fyeImg.find()){
                        pic = fyeImg.group(1);
                    }

                    try {
                        String result = title.replaceAll("[-+.^:!)(,]", "");
                        result = result.toLowerCase();
                        String ebay = String.format("https://www.ebay.com/sch/i.html?_nkw=%s&rt=nc&LH_Sold=1&LH_Complete=1", URLEncoder.encode(result, "UTF-8"));
                        String stash = String.format("https://stashpedia.com/search?terms=%s", URLEncoder.encode(result, "UTF-8"));

                        logger.info("IN STOCK");

                        DemandwareBuilder.buildAttachments(attachmentCreater, url, pic, title, price, qtyStr, ebay, stash, formatNames);
                    } catch (Exception e){
                        logger.error(EXCEPTION_LOG_MESSAGE, e);

                    }
                }
            }
        } else if(responseString.contains("<h1>Page Not Found</h1>") || responseString.contains("page you are looking for could not be found")) {
            logger.info("N/A - " + url);

            stockTracker.setOOS(url);
        } else {
            logger.info("ERROR - " + url);
        }
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setSku(String sku){
        this.sku = sku;
    }
}
