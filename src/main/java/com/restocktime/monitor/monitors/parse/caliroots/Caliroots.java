package com.restocktime.monitor.monitors.parse.caliroots;

import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.sns.SNSProductResponseParser;
import com.restocktime.monitor.monitors.parse.sns.SnsResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Caliroots implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SnsResponseParser.class);
    private StockTracker stockTracker;
    private List<String> formatNames;
    private final Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private DiscordLog discordLog;
    private String url;

    public Caliroots(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
        this.discordLog = new DiscordLog(Caliroots.class);
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        logger.info(responseString);
        if(responseString.contains("Choose size")){
            if(stockTracker.notifyForObject(url, isFirst)) {
                String title = "HYPE SHIT";
                Matcher m = titlePattern.matcher(responseString);
                if(m.find())
                    title = m.group(1);
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Caliroots", title, formatNames);
            }
        } else if(responseString.contains("<p class=\"sold-out\">")){
            stockTracker.setOOS(url);
        }
    }
}
