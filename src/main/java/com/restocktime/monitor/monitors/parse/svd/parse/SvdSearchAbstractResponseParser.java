package com.restocktime.monitor.monitors.parse.svd.parse;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.ingest.svd.SVD;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvdSearchAbstractResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SVD.class);
    private StockTracker stockTracker;
    private String url;
    private String name;
    private List<String> formatNames;

    String patternStr = "<title>(.*)</title>";
    Pattern pattern = Pattern.compile(patternStr);

    String linkPatternStr = "https://twitter.com/home\\?status=([^\"]*)";
    Pattern linkPattern = Pattern.compile(linkPatternStr);

    String searchNameStr = "alt=\"([^\"]*)\"";
    Pattern searchName = Pattern.compile(searchNameStr);


    public SvdSearchAbstractResponseParser(StockTracker stockTracker, String url, String name, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.name = name;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        Matcher m = pattern.matcher(responseString);
        Matcher link = linkPattern.matcher(responseString);
        Matcher title = searchName.matcher(responseString);

        if (m.find() && link.find()) {
            String productName = m.group(1).replaceAll("\\s+", " ").trim().toUpperCase();
            if (productName.equals("404 NOT FOUND")) {
                return;
            }


            if (stockTracker.notifyForObject(url, isFirst)) {
             //   attachmentCreater.addMessages(url, productName, "SVD", null, null);
            }

            logger.info("product found! " + productName);
            return;
        }
    }
}
