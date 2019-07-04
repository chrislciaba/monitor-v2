package com.restocktime.monitor.monitors.parse.sns;

import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SNSProductResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(SnsResponseParser.class);
    private StockTracker stockTracker;
    private List<String> formatNames;
    private final Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private DiscordLog discordLog;


    private final String SNS_TEMPLATE = "https://www.sneakersnstuff.com%s";
    private String url;


    public SNSProductResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
        this.discordLog = new DiscordLog(SNSProductResponseParser.class);
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();
        logger.info(responseString);
        if(responseString.contains("product-form__btn")){
            if(stockTracker.notifyForObject(url, isFirst)) {
                String title = "HYPE SHIT";
                Matcher m = titlePattern.matcher(responseString);
                if(m.find())
                    title = m.group(1);
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "SNS", title, formatNames);
                discordLog.info("@here BACK IN STOCK " + url);
            }
            discordLog.debug("in stock don't notify " + url);

        } else if(responseString.contains("Sold out") || responseString.contains("This raffle is closed")){
            stockTracker.setOOS(url);
            discordLog.info("OOS " + url);

        }
    }
}
