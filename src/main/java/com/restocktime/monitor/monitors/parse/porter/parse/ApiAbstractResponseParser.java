package com.restocktime.monitor.monitors.parse.porter.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.monitors.parse.porter.model.PorterProduct;
import com.restocktime.monitor.monitors.parse.porter.model.PorterSku;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.ingest.porter.Porter;
import com.restocktime.monitor.monitors.parse.porter.helper.PorterHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiAbstractResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(Porter.class);
    private Map<String, Map<String, Map<String, String>>> ALL_URLS;
    private StockTracker stockTracker;
    private String key;
    private String locale;
    private String sku;
    private List<String> formatNames;

    public ApiAbstractResponseParser(StockTracker stockTracker, String key, String locale, String sku, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.key = key;
        this.locale = locale;
        this.sku = sku;
        this.ALL_URLS = PorterHelper.initPorterMap();
        this.formatNames = formatNames;
    }

    final String SLACK_TEMPLATE = "<%s|ATC> / <%s|Wishlist> / <%s|Cyber>";
    final String DISCORD_TEMPLATE = "[ATC](%s) / [Wishlist](%s) / [Cyber](%s)";


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody();

        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordDiscordFields = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        if(responseString.contains("Requested product not found")){
            logger.info("Not found yet");
            return;
        }
        try {
            PorterProduct porterProduct = objectMapper.readValue(responseString, PorterProduct.class);
            for (PorterSku porterSku : porterProduct.getSkus()) {
                if (!porterSku.getStockLevel().contains("Out_of_Stock")) {
                    if (stockTracker.notifyForObject(porterSku.getId(), isFirst)) {
                        String atcLink = String.format(
                                ALL_URLS
                                        .get(key)
                                        .get(locale)
                                        .get("ATC"),
                                porterSku
                                        .getId());
                        String wishLink = String.format(ALL_URLS.get(key).get(locale).get("WISH"), porterSku.getId());
                        String cyberLink = ALL_URLS.get(key).get(locale).get("CYBER") + porterProduct.getId();
                        slackFields.add(new SlackField(porterSku.getDisplaySize(), String.format(SLACK_TEMPLATE, atcLink, wishLink, cyberLink)));
                        discordDiscordFields.add(new DiscordField(porterSku.getDisplaySize(), String.format(DISCORD_TEMPLATE, atcLink, wishLink, cyberLink), false));
                    }
                } else {
                    stockTracker.setOOS(porterSku.getId());
                }
            }


            if(slackFields.size() > 0){
                slackFields.add(new SlackField("Checkout Link", ALL_URLS.get(key).get(locale).get("CART")));
                discordDiscordFields.add(new DiscordField("Checkout Link", ALL_URLS.get(key).get(locale).get("CART"), false));
                String slackProductUrl = String.format(ALL_URLS.get(key).get(locale).get("PRODUCT"), sku);
             //   attachmentCreater.addMessages(slackProductUrl, porterProduct.getAnalyticsKey(), "Porter " + locale, slackFields, discordDiscordFields);
                DefaultBuilder.buildAttachments(attachmentCreater, slackProductUrl, null, locale +" Porter", porterProduct.getAnalyticsKey(), formatNames);
                return;
            }

        } catch (Exception e) {

        }
    }
}
