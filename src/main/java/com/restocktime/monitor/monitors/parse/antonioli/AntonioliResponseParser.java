package com.restocktime.monitor.monitors.parse.antonioli;

import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Pattern;

public class AntonioliResponseParser implements AbstractResponseParser {
    final static Logger log = Logger.getLogger(AntonioliResponseParser.class);


    private final String patternStr = "<a itemProp=\"url\" href=\"([^\"]*)\"><span content='([^']*)'";
    private final String FORMAT_URL = "https://www.antonioli.eu/%s";
    private Pattern pattern;
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private String url;
    private String name;
    private List<String> formatNames;

    public AntonioliResponseParser(String url, String name, StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        pattern = Pattern.compile(patternStr);
        this.url = url;
        this.name = name;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if((responseString.contains("data-product-id") || responseString.contains("add-to-cart"))){
            log.info("in stock");
            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Antonioli", name, formatNames);
            }
        } else if(responseString.contains("<body class=\"404\">") || responseString.contains("card products_search_results_heading_no_results_found")){
            stockTracker.setOOS(url);
            log.info("oos");

        }
    }
}
