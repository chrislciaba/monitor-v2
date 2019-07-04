package com.restocktime.monitor.monitors.parse.footdistrict.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FootdistrictParseSitemapResponse implements AbstractResponseParser {

    private StockTracker stockTracker;
    private String searchPatternStr = "https://footdistrict\\.com/([^.]*)\\.html";
    private Pattern searchPattern;
    private boolean foundAll;
    private List<String> formatNames;

    public FootdistrictParseSitemapResponse(StockTracker stockTracker, List<String> formatNames){
        this.stockTracker = stockTracker;
        searchPattern = Pattern.compile(searchPatternStr);
        this.formatNames = formatNames;
        this.foundAll = false;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        String responseString = basicHttpResponse.getBody();
        Matcher m = searchPattern.matcher(responseString);
        boolean found = responseString.contains("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        while(m.find()){
            String link = m.group(0);

            String title = m.group(1).replaceAll("-", " ").toUpperCase();
            if(stockTracker.notifyForObject(link, isFirst) && foundAll) {
                DefaultBuilder.buildAttachments(attachmentCreater, link, null,"FootDistrict 2", title, formatNames);
            }
        }

        if(found){
            foundAll = true;
        }
    }
}
