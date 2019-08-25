package com.restocktime.monitor.monitors.parse.aio.soto;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SotoResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SotoResponseParser.class);
    private final String URL_TEMPLATE = "https://www.sotostore.com%s";
    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private final String namePatternStr = "<a href=\"([^\"]*)\" class=\"card-content\"><h4 class=\"card-brand\">([^<]*)</h4><h4 class=\"card-title\">([^<]*)</h4>";
    private Pattern pattern = Pattern.compile(namePatternStr);
    private List<String> formatNames;

    public SotoResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s*<", "><");
        Matcher products = pattern.matcher(responseString);
        while(products.find()){
            String url = products.group(1);
            String brand = products.group(2).trim();
            String name = products.group(3).trim();
            if(keywordSearchHelper.search(name + " " + brand) && stockTracker.notifyForObject(url, isFirst)){
             //   attachmentCreater.addMessages(String.format(URL_TEMPLATE, url), brand + " - " + name, "Soto", null, null);
                DefaultBuilder.buildAttachments(attachmentCreater, name, null, "Soto", name, formatNames);
            }
        }
    }
}
