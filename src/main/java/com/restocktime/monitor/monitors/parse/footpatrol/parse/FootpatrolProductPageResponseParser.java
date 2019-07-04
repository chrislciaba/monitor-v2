package com.restocktime.monitor.monitors.parse.footpatrol.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.footpatrol.attachment.FootpatrolBuilder;
import com.restocktime.monitor.monitors.parse.footpatrol.model.Item;
import com.restocktime.monitor.monitors.parse.footpatrol.model.ProductResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FootpatrolProductPageResponseParser implements AbstractResponseParser {
    private StockTracker stockTracker;
    private Pattern pattern = Pattern.compile("<script type=\"text/javascript\">\\s+var\\s+dataObject\\s+=\\s+([^;]*);\\s*</script>");
    private List<String> formatNames;
    private KeywordSearchHelper keywordSearchHelper;

    public FootpatrolProductPageResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.keywordSearchHelper = keywordSearchHelper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();

        Matcher sizeMatcher = pattern.matcher(responseString);

        if(sizeMatcher.find()){
            String s = sizeMatcher.group(1);

            s = s.replaceAll("\\s+([A-Za-z0-9]*):", " \"$1\":").replaceAll("//[A-Za-z ]*", "").replaceAll("\\[search,listing]", "").replaceAll("- list/search/featured", "");

            try {
                ProductResponse productResponse = new ObjectMapper().readValue(s, ProductResponse.class);
                for(Item i : productResponse.getItems()){

                    if(keywordSearchHelper.search(i.getDescription()) && stockTracker.notifyForObject(i.getPlu(), isFirst)){
                        FootpatrolBuilder.buildAttachments(attachmentCreater, i.getPlu(), null, "Footpatrol", i.getDescription(), formatNames);
                    }
                }
            } catch (Exception e){

            }
        }



    }
}