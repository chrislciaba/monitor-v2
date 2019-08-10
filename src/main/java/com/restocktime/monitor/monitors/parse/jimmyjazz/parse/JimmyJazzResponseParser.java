package com.restocktime.monitor.monitors.parse.jimmyjazz.parse;

import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.monitors.parse.rimowa.RimowaResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JimmyJazzResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(RimowaResponseParser.class);
    private StockTracker stockTracker;
    private String url;
    private final Pattern sizePattern = Pattern.compile("<a href=\"#\" id=\"itemcode_[0-9]*\" class=\"box\">([^<]*)</a>");
    private final Pattern titlePattern = Pattern.compile("<title>(.*)</title>");
    private final Pattern linkPatern = Pattern.compile("<link rel=\"canonical\" href=\"([^\"]*)\"");
    private List<String> formatNames;

    public JimmyJazzResponseParser(StockTracker stockTracker, String url, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        Matcher sizeMatcher = sizePattern.matcher(responseString);

        List<String> sizeList = new ArrayList<>();
        while(sizeMatcher.find()){
            sizeList.add(sizeMatcher.group(1));
        }

        if(sizeList.isEmpty()){
            if(responseString.contains("box piunavailable") || responseString.contains("<h2>SEARCH RESULTS</h2>")){
                logger.info("oos " + url);
                stockTracker.setOOS(url);
            } else {
                logger.info("something weird happened " + url);
            }
        } else {
            if(stockTracker.notifyForObject(url, isFirst)){
                Matcher titleMatcher = titlePattern.matcher(responseString);
                String title = "NAME UNAVAILABLE";
                if(titleMatcher.find()){
                    title = titleMatcher.group(1);
                }

                Matcher linkMatcher = linkPatern.matcher(responseString);
                String link = url;
                if(linkMatcher.find()){
                    link = linkMatcher.group(1);
                }

                String sizes = String.join(" / ", sizeList);
                List<DiscordField> discordFields = new ArrayList<>();
                List<SlackField> slackFields = new ArrayList<>();
                discordFields.add(new DiscordField("Available Sizes", sizes, false));
                slackFields.add(new SlackField("Available Sizes", sizes));
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Jimmy Jazz", title, formatNames);
             //   attachmentCreater.addMessages(link, title, "JimmyJazz", slackFields, discordFields);
            }

        }


    }
}
