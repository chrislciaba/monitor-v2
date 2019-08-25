package com.restocktime.monitor.monitors.parse.aio.citygear;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CitygearAbstractResponseParser implements AbstractResponseParser {

    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    Pattern pattern = Pattern.compile("<a href=\"([^\"]*)\" title=\"([^\"]*)\" class=\"product-image\">");
    private List<String> formatNames;


    public CitygearAbstractResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames){
        this.keywordSearchHelper = keywordSearchHelper;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String s = basicHttpResponse.getBody().get();

        Matcher m = pattern.matcher(s);
        if(m.find()){
            String name = m.group(2);
            String url = m.group(1);

            if(keywordSearchHelper.search(name) && stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, null,"Citygear", name, formatNames);
            }
        }
    }

}
