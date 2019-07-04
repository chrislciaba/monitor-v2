package com.restocktime.monitor.monitors.parse.unionjordan;

import com.restocktime.monitor.helper.botlinkgen.BotLinkGen;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnionResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(UnionResponseParser.class);
    private StockTracker stockTracker;
    private final Pattern namePattern = Pattern.compile("<a class=\"grid-view-item__link grid-view-item__image-container full-width-link\" href=\"([^\"]*)\"><span class=\"visually-hidden\">([^<]*)</span>");
    private final Pattern imgPattern = Pattern.compile("lazyload\"\\s+src=\"([^\"]*)\"");
    private List<String> formatNames;

    public UnionResponseParser(StockTracker stockTracker){
        this.stockTracker = stockTracker;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().replaceAll(">\\s+<", "><").replaceAll("\\s+", " ");
        Matcher m = namePattern.matcher(responseString);
        Matcher n = imgPattern.matcher(responseString);
        while(m.find() && n.find()){
            String link = m.group(1);
            String name = m.group(2);
            String img = "https:" + n.group(1);

            if(stockTracker.notifyForObject(link, isFirst)){
                String url = "https://unionjordan.com" + link;
                List<SlackField> slackFields = new ArrayList<>();
                List<DiscordField> discordFields = new ArrayList<>();
                slackFields.add(0, new SlackField("Random Size", BotLinkGen.genShopifySlackRandom(url)));
                discordFields.add(0, new DiscordField("Random Size", BotLinkGen.genShopifyDiscordRandom(url), false));
               // attachmentCreater.addMessages(url, name, "Union front end", slackFields, discordFields, img);
            }
        }

    }
}
