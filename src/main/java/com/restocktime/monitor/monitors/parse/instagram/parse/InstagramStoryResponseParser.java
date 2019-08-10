package com.restocktime.monitor.monitors.parse.instagram.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.instagram.attachment.DefaultInstagramStory;
import com.restocktime.monitor.monitors.parse.instagram.model.stories.EntryPoint;
import com.restocktime.monitor.monitors.parse.instagram.model.stories.Story;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;

public class InstagramStoryResponseParser implements AbstractResponseParser {
    private ObjectMapper objectMapper;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private String name;
    private DiscordLog discordLog;


    public InstagramStoryResponseParser(ObjectMapper objectMapper, StockTracker stockTracker, List<String> formatNames, String name) {
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.name = name;
        discordLog = new DiscordLog(InstagramStoryResponseParser.class);
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }


        try {

            EntryPoint entryPoint = objectMapper.readValue(basicHttpResponse.getBody().get(), EntryPoint.class);
            for(Story s : entryPoint.getData().getReels_media().get(0).getItems()){

                if(stockTracker.notifyForObject(s.getId(), isFirst)){
                    for(String format : formatNames) {
                        DefaultInstagramStory.addAttachment(attachmentCreater, s.getStory_cta_url(), name, s.getDisplay_url(), format);
                    }
                }
            }

        } catch (Exception e){

        }
    }
}
