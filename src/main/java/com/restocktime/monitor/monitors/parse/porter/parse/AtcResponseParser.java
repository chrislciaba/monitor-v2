package com.restocktime.monitor.monitors.parse.porter.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.monitors.parse.porter.model.AtcRes;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.monitors.ingest.porter.Porter;
import com.restocktime.monitor.monitors.parse.porter.helper.PorterHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtcResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(Porter.class);


    private Map<String, Map<String, Map<String, String>>> ALL_URLS;
    private StockTracker stockTracker;
    private String key;
    private String locale;
    private String sku;
    private String url;
    private String name;

    public AtcResponseParser(StockTracker stockTracker, String key, String locale, String sku, String url, String name) {
        this.stockTracker = stockTracker;
        this.key = key;
        this.locale = locale;
        this.sku = sku;
        this.url = url;
        this.name = name;
        this.ALL_URLS = PorterHelper.initPorterMap();
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains("410 Gone")){
            logger.info("Gone - " + url);
            return;
        }

        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordDiscordFields = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AtcRes atcRes = objectMapper.readValue(responseString, AtcRes.class);
            if (atcRes.getResult().contains("PRODUCT_ADDED")) {
                String cyberLink = ALL_URLS.get(key).get(locale).get("CYBER") + sku;

                discordDiscordFields.add(new DiscordField("Cart Link", url, false));
                discordDiscordFields.add(new DiscordField("Checkout Link", ALL_URLS.get(key).get(locale).get("CART"), false));
                discordDiscordFields.add(new DiscordField("Cyber", cyberLink, false));

                slackFields.add(new SlackField("Cart Link", url));
                slackFields.add(new SlackField("Checkout Link", ALL_URLS.get(key).get(locale).get("CART")));
                slackFields.add(new SlackField("Cyber", cyberLink));


                logger.info("PRODUCT_ADDED");

                if (stockTracker.notifyForObject(sku, isFirst)) {
                //    attachmentCreater.addMessages(String.format(ALL_URLS.get(key).get(locale).get("PRODUCT"), sku),
                //    name, "Porter " + locale, slackFields, discordDiscordFields);
                }

                return;
            } else {
                logger.info(atcRes.getResult() + " - " + url);
            }
        } catch (Exception e){

        }
    }

}
