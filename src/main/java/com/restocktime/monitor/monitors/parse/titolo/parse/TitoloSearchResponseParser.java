package com.restocktime.monitor.monitors.parse.titolo.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitoloSearchResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(TitoloSearchResponseParser.class);

    String linkPatternStr = "class=\"product-name[^\"]*\" href=\"([^\"]*)\" title=\"([^\"]*)\"";
    Pattern linkPattern = Pattern.compile(linkPatternStr);
    private StockTracker stockTracker;
    private List<String> formatNames;

    public TitoloSearchResponseParser(StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();
        Matcher link = linkPattern.matcher(responseString);

        if (responseString.contains("did not match any products")) {
            return;
        } else if (link.find()) {

            if (stockTracker.notifyForObject(link.group(2), isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, link.group(1), null, "Titolo", link.group(2), formatNames);
            }

            logger.info("product found! " + link.group(2));
        }
    }

}
