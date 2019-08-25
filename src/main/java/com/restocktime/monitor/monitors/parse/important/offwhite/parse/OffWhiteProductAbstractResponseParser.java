package com.restocktime.monitor.monitors.parse.important.offwhite.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.helper.botlinkgen.BotLinkGen;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.monitors.parse.important.offwhite.model.offwhite.OffWhiteJson;
import com.restocktime.monitor.monitors.parse.important.offwhite.model.offwhite.OffWhiteSize;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class OffWhiteProductAbstractResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(OffWhiteProductAbstractResponseParser.class);

    private StockTracker stockTracker;
    private String url;
    private String name;
    private List<String> formatNames;

    public OffWhiteProductAbstractResponseParser(StockTracker stockTracker, String url, String name, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.name = name;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            OffWhiteJson offWhiteJson = objectMapper.readValue(responseString, OffWhiteJson.class);

            if(offWhiteJson.getAvailable_sizes().size() == 0){
                logger.info("Out of stock: " + url);
            } else {
                List<SlackField> slackSlackFields = new ArrayList<>();
                List<DiscordField> discordDiscordFields = new ArrayList<>();

                slackSlackFields.add(new SlackField("Bot Links", BotLinkGen.genOffWhiteSlackLink(url)));
                discordDiscordFields.add(new DiscordField("Bot Links", BotLinkGen.genOffWhiteDiscordLink(url), false));
                for(OffWhiteSize offWhiteSize : offWhiteJson.getAvailable_sizes()){
                    if(stockTracker.notifyForObject(Integer.toString(offWhiteSize.getId()), false)) {
                        slackSlackFields.add(new SlackField("Size", offWhiteSize.getName()));
                        discordDiscordFields.add(new DiscordField("Size", offWhiteSize.getName(), false));
                    }
                }
                logger.info("In Stock: " + url);
                if(stockTracker.notifyForObject(url, false) && !discordDiscordFields.isEmpty()) {
                    DefaultBuilder.buildAttachments(attachmentCreater, url, null, "OFF WHITE", name, formatNames);
               //     attachmentCreater.addMessages(url, name, "Off White", slackSlackFields, discordDiscordFields);
                } else {
                    stockTracker.setOOS(url);
                }
            }
        } catch (Exception e){

        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }
}
