package com.restocktime.monitor.monitors.parse.important.mesh.parse;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.ops.log.WebhookType;
import com.restocktime.monitor.util.ops.metrics.MonitorMetrics;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.mesh.attachment.FootpatrolBuilder;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MeshSearchResponseParser implements AbstractResponseParser {
    private static final Logger log = Logger.getLogger(MeshSearchResponseParser.class);

    private StockTracker stockTracker;
    private Pattern pattern = Pattern.compile("<meta property=\"og:url\" content=\"([^\"]*)\"/>");
    private Pattern namePattern = Pattern.compile("<meta property=\"og:title\" content=\"([^\"]*)\"/>");
    private Pattern imgPattern = Pattern.compile("<meta property=\"og:image\" content=\"([^\"]*)\"/>");
    private List<String> formatNames;
    private String url;
    private MonitorMetrics monitorMetrics;

    public MeshSearchResponseParser(String url, StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.url = url;
        this.monitorMetrics = new MonitorMetrics(WebhookType.MESH, url);
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            log.info("timeout");
            monitorMetrics.ban();
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains("<meta property=\"og:type\" content=\"product\"/>")){
                if(stockTracker.notifyForObject(url, false)){

                    Matcher m = pattern.matcher(responseString);
                    Matcher nameMatcher = namePattern.matcher(responseString);
                    Matcher imgMatcher = imgPattern.matcher(responseString);
                    String link, name, img;
                    if (m.find()) {
                        link = m.group(1);
                    } else {
                        link = url;
                    }

                    if (nameMatcher.find()) {
                        name = nameMatcher.group(1);
                    } else {
                        name = "NAME NOT FOUND";
                    }

                    if (imgMatcher.find()) {
                        img = imgMatcher.group(1);
                    } else {
                        img = null;
                    }

                    FootpatrolBuilder.buildAttachments(attachmentCreater, link, img, "Mesh 1", name, formatNames);
                }
                monitorMetrics.success();
        } else if (responseString.contains("<meta property=\"og:type\" content=\"website\"/>")) {
            monitorMetrics.success();
            stockTracker.setOOS(url);
        } else if (responseString.contains("Access Denied")) {
            monitorMetrics.ban();
        } else {
            monitorMetrics.error();
        }




    }
}
