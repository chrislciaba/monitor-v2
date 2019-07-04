package com.restocktime.monitor.monitors.parse.dsm.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.botlinkgen.BotLinkGen;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.password.PasswordHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.ShopifyJson;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DsmProductResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(DsmProductResponseParser.class);

    private String url;
    private StockTracker stockTracker;
    private String region;
    private final String jsonPatternStr = "<script type=\"application/json\" id=\"ProductJson-product-template\">\\s*(.*)\\s*</script>";

    private Pattern jsonPattern;
    private ObjectMapper objectMapper;
    private List<String> formatNames;
    private boolean isPassUp;

    public DsmProductResponseParser(StockTracker stockTracker, String url, String region, ObjectMapper objectMapper, List<String> formatNames){
        this.url = url;
        this.region = region;
        this.stockTracker = stockTracker;
        this.jsonPattern = Pattern.compile(jsonPatternStr);
        this.objectMapper = objectMapper;
        this.formatNames = formatNames;
        this.isPassUp = false;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) throws Exception {
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }
        String s = basicHttpResponse.getBody();

        Matcher m = jsonPattern.matcher(basicHttpResponse.getBody());

        isPassUp = PasswordHelper.getPassStatus(attachmentCreater, basicHttpResponse, url, isFirst, isPassUp, formatNames);

        if(!m.find()){
            logger.info("Password page up or something - " + url);
            return;
        }

        String json = m.group(1).trim();
        ShopifyJson shopifyJson;

        shopifyJson = objectMapper.readValue(json, ShopifyJson.class);

        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordDiscordFields = new ArrayList<>();

        for(Variant variant : shopifyJson.getVariants()){
            if(stockTracker.notifyForObject(variant.getId(), isFirst)){
                String discordMessage = BotLinkGen.genShopifyDiscordWithSize(url, variant.getId());
                String slackMessage = BotLinkGen.genShopifySlackWithSizes(url, variant.getId());
                slackFields.add(new SlackField(variant.getTitle(), slackMessage));
                discordDiscordFields.add(new DiscordField(variant.getTitle(), discordMessage, false));
            }
        }

        if(slackFields.size() > 0){
            String discordMessage = BotLinkGen.genShopifyDiscordRandom(url);
            String slackMessage = BotLinkGen.genShopifySlackRandom(url);
            slackFields.add(0, new SlackField("Random Size", slackMessage));
            discordDiscordFields.add(0, new DiscordField("Random Size", discordMessage, false));
            //attachmentCreater.addMessages(url, shopifyJson.getTitle(), "DSM " + region, slackFields, discordDiscordFields, "http:" + shopifyJson.getFeatured_image().replaceAll("\\\\", ""));
        }
    }
}
