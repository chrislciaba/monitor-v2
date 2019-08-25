package com.restocktime.monitor.monitors.parse.important.offwhite.parse;

import com.restocktime.monitor.util.helper.botlinkgen.BotLinkGen;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OffWhiteHtmlPageResponseParser {
    final static Logger logger = Logger.getLogger(OffWhiteProductAbstractResponseParser.class);

    private StockTracker stockTracker;
    private String url;
    private String name;
    private Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private Pattern sizePattern = Pattern.compile("<label for=\"variant_id_[0-9]*\">([^<]*)</label>");
    private List<String> formatNames;

    public OffWhiteHtmlPageResponseParser(StockTracker stockTracker, String url, String name, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.name = name;
        this.formatNames =formatNames;
    }
    //<label for="variant_id_107700">M</label>


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        String productName = name;
        Matcher titleMatcher = titlePattern.matcher(responseString);
        Matcher sizeMatcher = sizePattern.matcher(responseString);
        List<String> list = new ArrayList<>();
        while(sizeMatcher.find()){
            logger.info(sizeMatcher.group(1));
            list.add(sizeMatcher.group(1));
        }

        if(list.size() > 0 && stockTracker.notifyForObject(url, isFirst)){
            String sizes = String.join(", ", list);
            if(titleMatcher.find()){
                productName = titleMatcher.group(1);
            }
            List<SlackField> slackSlackFields = new ArrayList<>();
            List<DiscordField> discordDiscordFields = new ArrayList<>();

            slackSlackFields.add(new SlackField("Bot Links", BotLinkGen.genOffWhiteSlackLink(url)));
            discordDiscordFields.add(new DiscordField("Bot Links", BotLinkGen.genOffWhiteDiscordLink(url), false));

            slackSlackFields.add(new SlackField("Available Sizes", sizes));
            discordDiscordFields.add(new DiscordField("Available Sizes", sizes, false));
            DefaultBuilder.buildAttachments(attachmentCreater, UrlHelper.urlRandNumberAppended(url), null, "Off White 2", productName, formatNames);

            //  attachmentCreater.addMessages(UrlHelper.urlRandNumberAppended(url), productName, "Off White 2", slackSlackFields, discordDiscordFields);
        } else if(list.size() == 0) {
            stockTracker.setOOS(url);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }
}
