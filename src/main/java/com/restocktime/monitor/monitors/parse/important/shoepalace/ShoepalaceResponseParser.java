package com.restocktime.monitor.monitors.parse.important.shoepalace;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.aio.rimowa.RimowaResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.ops.log.DiscordLog;
import com.restocktime.monitor.util.ops.log.WebhookType;
import com.restocktime.monitor.util.ops.metrics.MonitorMetrics;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShoepalaceResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(RimowaResponseParser.class);
    private StockTracker stockTracker;
    private String url;
    private final String namePatternStr = "<title class=\"notranslate\">([^<]*)</title>";
    private Pattern pattern;
    private List<String> formatNames;
    private MonitorMetrics monitorMetrics;

    public ShoepalaceResponseParser(StockTracker stockTracker, String url, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        pattern = Pattern.compile(namePatternStr);
        this.formatNames = formatNames;
        this.monitorMetrics = new MonitorMetrics(WebhookType.SP, url);


    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            monitorMetrics.error();
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        String name = "NAME_UNAVAILABLE";
        if(responseString.contains("alt=\"Add to Cart\"")){
            monitorMetrics.success();
            Matcher m = pattern.matcher(responseString);
            logger.info("in stock");
            if(m.find()){
                name = m.group(1);
            }
            if(stockTracker.notifyForObject(url, isFirst)){
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Shoepalace", name, formatNames);
            }
        } else if(responseString.contains("Currently out-of-stock.")){
            monitorMetrics.success();
            logger.info("OOS - " + url);
            stockTracker.setOOS(url);
        } else if(responseString.contains("<title>403 Forbidden</title>")){
            monitorMetrics.ban();
        } else {
            DiscordLog.log(WebhookType.SP, responseString.substring(0, Math.min(responseString.length(), 1000)));
            monitorMetrics.error();
        }
    }
}
