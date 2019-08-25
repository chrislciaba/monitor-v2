package com.restocktime.monitor.monitors.parse.aio.demandware.parse;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.important.offwhite.parse.OffWhiteAtcResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DemandwareResponseParser {

    private StockTracker stockTracker;
    private final Pattern cartItemPattern = Pattern.compile("<div class=\"mini-cart-name\"><a href=\"([^\"]*)\">([^<]*)</a>");
    private String url;


    final static Logger logger = Logger.getLogger(OffWhiteAtcResponseParser.class);


    public DemandwareResponseParser(StockTracker stockTracker, String url) {
        this.stockTracker = stockTracker;
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains("<div class=\"price-adjusted-total\">") && stockTracker.notifyForObject(url, isFirst)){
            Matcher product = cartItemPattern.matcher(responseString.replaceAll(">\\s+<", "><"));
            if(product.find()){

            }

               // attachmentCreater.addMessages(product.group(1), product.group(2), "Demandware", null, null);
        } else if(responseString.contains("shoppingBagEmpty")){
            logger.info(responseString);

            stockTracker.setOOS(url);
        } else {
            logger.info(responseString);
        }
    }
}
