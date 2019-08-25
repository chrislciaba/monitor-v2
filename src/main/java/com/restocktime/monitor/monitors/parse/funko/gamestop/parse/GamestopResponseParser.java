package com.restocktime.monitor.monitors.parse.funko.gamestop.parse;

import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamestopResponseParser  implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(GamestopResponseParser.class);
    private final String addtocartpresent = "<a onclick=\"AddToCartClicked(this);\"";
    private final String notAvailable = "class=\"ats-prodBuy-notAvail\"";
    private final String DESKTOP_ATC_TEMPLATE = "http://www.gamestop.com/gs/autoadd/?skus=%s";
    private final String MOBILE_ATC_TEMPLATE = "https://m.gamestop.com/Orders/AddToCart?sku=%s&quantity=1&affid=2121&cid=gsn_70000001";
    private final String IMAGE_URL = "https://www.gamestop.com/common/images/lbox/%sb.jpg";
    private final String GOOGLE_IMAGE = "https://images1-focus-opensocial.googleusercontent.com/gadgets/proxy?container=focus&url=%s";
    private final Pattern titlePattern = Pattern.compile("\"productName\":\"([^<]*)\",\"description\"");
    // private final Pattern imgPattern =
    // Pattern.compile("productImage\":\"([^\\\"]*)\",\"");
    private final Pattern skuPattern = Pattern.compile("\"productImage\":\"/common/images/lbox/([^\"]*)b");
    private final Pattern pricePattern = Pattern.compile("data-price=\" ([^\"]*)\"");
    private final Pattern oosPattern = Pattern.compile("Unavailable Online");

    private StockTracker stockTracker;
    private List<String> formatNames;
    private String url;
    private HttpRequestHelper httpRequestHelper;
    private String subtotal = "subtotal";

    public GamestopResponseParser(String url, StockTracker stockTracker, List<String> formatNames,
            HttpRequestHelper httpRequestHelper) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.url = url;
        this.httpRequestHelper = httpRequestHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse,/* BasicRequestClient basicRequestClient,*/
            AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }
        isFirst = false;

        String responseString = basicHttpResponse.getBody().get();

        Matcher oosMatcher = oosPattern.matcher(responseString);

        Matcher skuMatcher = skuPattern.matcher(responseString);
        String sku = "";

        if (skuMatcher.find()) {
            sku = skuMatcher.group(1);
        }

        String desktopATC = String.format(DESKTOP_ATC_TEMPLATE, sku);
        String mobileATC = String.format(MOBILE_ATC_TEMPLATE, sku);
        return;
      /*  try {
            BasicHttpResponse desktopHttpResponse = httpRequestHelper.performGet(basicRequestClient, desktopATC);
            BasicHttpResponse mobileHttpResponse = httpRequestHelper.performGetNoRedirects(basicRequestClient,
                    mobileATC);

            if (desktopHttpResponse == null || desktopHttpResponse.getBody() == null || mobileHttpResponse == null
                    || mobileHttpResponse.getBody() == null) {
                return;
            }

            String desktopResponseString = desktopHttpResponse.getBody();
            String mobileResponseString = mobileHttpResponse.getBody();
            // System.out.println(mobileResponseString);
            // if(mobileResponseString.contains("checkout") &&
            // mobileResponseString.contains("sorry but there was an error adding the
            // product to your cart")){
            // System.out.println("OOS: " + url);
            // } else if (mobileResponseString.contains("checkout") &&
            // !mobileResponseString.contains("sorry but there was an error adding the
            // product to your cart")){
            // System.out.println("IN STOCK: " + url);
            // }
            // System.out.println(mobileResponseString);

            if (desktopResponseString.contains(subtotal) || mobileResponseString.contains("checkout")
                    && !mobileResponseString.contains("sorry but there was an error adding the product to your cart")) {
                Matcher titleMatcher = titlePattern.matcher(responseString);
                Matcher priceMatcher = pricePattern.matcher(responseString);
                // Matcher imgMatcher = imgPattern.matcher(responseString);
                String name = titleMatcher.find() ? titleMatcher.group(1) : "N/A";
                String price = priceMatcher.find() ? priceMatcher.group(1) : "N/A";
                // String img = imgMatcher.find()?imgMatcher.group(1):null;
                String img = String.format(IMAGE_URL, sku);
                // System.out.println("IN STOCK: " + url);
                String result = name.replaceAll("[-+.^:!)(,]", "");
                result = result.toLowerCase();
                String ebay = String.format("https://www.ebay.com/sch/i.html?_nkw=%s&rt=nc&LH_Sold=1&LH_Complete=1", URLEncoder.encode(result, "UTF-8"));
                String stash = String.format("https://stashpedia.com/search?terms=%s", URLEncoder.encode(result, "UTF-8"));

                if (stockTracker.notifyForObject(url, isFirst)) {
                    GamestopBuilder.buildAttachments(attachmentCreater, url, String.format(GOOGLE_IMAGE, img), name,
                            price, desktopATC, mobileATC, ebay, stash, formatNames);
                }
            } else if (!desktopResponseString.contains(subtotal) && mobileResponseString.contains("checkout")
                    && mobileResponseString.contains("sorry but there was an error adding the product to your cart")
                    && oosMatcher.find()) {
                logger.info("OOS - " + url);
                stockTracker.setOOS(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
