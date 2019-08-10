package com.restocktime.monitor.monitors.parse.frenzy.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.footsites.parse.FootsitesResponseParser;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.monitors.parse.frenzy.model.FrenzyProduct;
import com.restocktime.monitor.monitors.parse.frenzy.model.FrenzyResponse;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.monitors.parse.snkrs.parse.helper.DateHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class FrenzyResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(FootsitesResponseParser.class);
    private final String FRENZY_URL_TEMPLATE = "https://frenzy.sale/%s";

    private StockTracker stockTracker;
    private ObjectMapper objectMapper;
    private List<String> formatNames;

    public FrenzyResponseParser(StockTracker stockTracker, ObjectMapper objectMapper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.objectMapper = objectMapper;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        try{

            FrenzyResponse frenzyResponse = objectMapper.readValue(basicHttpResponse.getBody().get(), FrenzyResponse.class);
            for(FrenzyProduct frenzyProduct : frenzyResponse.getFlashsales()){
                List<SlackField> slackFields = new ArrayList<>();
                List<DiscordField> discordDiscordFields = new ArrayList<>();

                if(stockTracker.notifyForObject(frenzyProduct.getPassword(), isFirst)){
                    String name = frenzyProduct.getTitle().replaceAll("[^\\x00-\\x7F\\s]", "").toUpperCase();
                    String dateFormatted = DateHelper.formatDate(frenzyProduct.getStarted_at());
                    String hidden = frenzyProduct.getHidden()?"YES":"NO";
                    discordDiscordFields.add(new DiscordField("LAUNCH DATE", dateFormatted, false));
                    slackFields.add(new SlackField("LAUNCH DATE", dateFormatted));
                    discordDiscordFields.add(new DiscordField("HIDDEN", hidden, false));
                    slackFields.add(new SlackField("HIDDEN", hidden));
                    if(name.length() == 0)
                        name = "NAME UNAVAILABLE";
                    if(frenzyProduct.getImage_urls().isEmpty()){
                        DefaultBuilder.buildAttachments(attachmentCreater, String.format(FRENZY_URL_TEMPLATE, frenzyProduct.getPassword()), null, "Frenzy", name, formatNames);
                    } else {
                   //     attachmentCreater.addMessages(String.format(FRENZY_URL_TEMPLATE, frenzyProduct.getPassword()), name, "Frenzy", slackFields, discordDiscordFields, frenzyProduct.getImage_urls().get(0));
                    }
                }
            }
        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }
}
