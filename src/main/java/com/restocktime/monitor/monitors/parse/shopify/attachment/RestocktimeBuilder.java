package com.restocktime.monitor.monitors.parse.shopify.attachment;

import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestocktimeBuilder {
    private static final String CART_LINK = "%s/cart/%s:1";
    private static final String ATC_LINK = "%s/cart/add?id=%s";
    private static final String DSM_ATC_LINK = "%s/cart/add?id=%s&properties[%s]=%s";
    private static final String CYBER_LINK = "https://cybersole.io/dashboard/tasks?quicktask=%s";
    //private static final String WOP_LINK = "http://client.wopbot.com/createTask?url=%s";
    //private static final String WHAT_LINK = "https://whatbot.club/redirect-qt?qt=whatbot://%s";
    //private static final String PD_LINK = "https://restocktime-redirect.herokuapp.com/redirect?q=destroyer://%s";
    private static final String EVE_LINK = "https://restocktime-redirect.herokuapp.com/redirect?eveLink=%s";
    private static final String DASHE_LINK = "https://api.dashe.io/v1/actions/quicktask?url=%s";
   //private static final String SOLE_LINK = "http://localhost:4444/?url=%s";
    private static final String TKS_LINK = "https://thekickstationapi.com/quick-task.php?link=%s&autostart=true";
    private static final String TKS_ATC_LINK = "https://thekickstationapi.com/quick-task.php?link=%s&autostart=true&isatclink=true";


    static final String DISCORD_TEMPLATE = "[ATC](%s) | [CHECKOUT](%s)";
    static final String DISCORD_DSM = "[ATC](%s)";

    static final String DISCORD_RANDOM_TEMPLATE = "[Cyber](%s) | [TKS](%s) | [Dashe](%s)";
    static final String SLACK_TEMPLATE = "<%s|ATC> / <%s|Cyber> / <%s|TKS> / <%s|Dashe>";
    static final String SLACK_RANDOM_TEMPLATE = "<%s|Cyber> / <%s|TKS> / <%s|Dashe>";

    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, String price, List<Variant> variants, int num, String key, Optional<String> dsmKey, Optional<String> dsmValue) {
        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordFields = new ArrayList<>();

        for (Variant v : variants) {
            if (v.getAvailable()) {
                slackFields.add(new SlackField(v.getTitle(), genShopifySlackWithSizes(url, v.getId()), false));

                if(dsmKey.isPresent() && dsmValue.isPresent()) {
                    discordFields.add(new DiscordField(v.getTitle(), genShopifyDiscordWithSizeDsm(url, v.getId(), dsmKey.get(), dsmValue.get()), false));
                } else {
                    discordFields.add(new DiscordField(v.getTitle(), genShopifyDiscordWithSize(url, v.getId()), false));
                }
            }
        }

        slackFields.add(new SlackField("Price (Approximate):", price, false));
        slackFields.add(new SlackField("Random Size", genShopifySlackRandom(url)));
        discordFields.add(new DiscordField("Price (Approximate):", price, false));
        discordFields.add(new DiscordField("Random Size", genShopifyDiscordRandom(url), false));
        attachmentCreater.addMessages(url, "", name, "Shopify " + num + " (" + UrlHelper.getHost(url) + ")", slackFields, discordFields, imgUrl, null, key);

    }

    public static String genShopifyDiscordWithSize(String url, String variant){
        String checkoutLink = String.format(CART_LINK, UrlHelper.deriveBaseUrl(url), variant);
        String atcLink = String.format(ATC_LINK, UrlHelper.deriveBaseUrl(url), variant);

        String cyberLink = String.format(CYBER_LINK, atcLink);
        //String tksLink = String.format(TKS_ATC_LINK, atcLink);
        //String dasheLink = String.format(DASHE_LINK, atcLink);
        return String.format(DISCORD_TEMPLATE, atcLink, checkoutLink);
    }

    public static String genShopifyDiscordWithSizeDsm(String url, String variant, String key, String value){
        String atcLink = String.format(DSM_ATC_LINK, UrlHelper.deriveBaseUrl(url), variant, key, value);
        return String.format(DISCORD_DSM, atcLink);
    }

    public static String genShopifySlackWithSizes(String url, String variant){
        String atcLink = String.format(CART_LINK, UrlHelper.deriveBaseUrl(url), variant);
        String cyberLink = String.format(CYBER_LINK, atcLink);
        String tksLink = String.format(TKS_ATC_LINK, atcLink);
        String dasheLink = String.format(DASHE_LINK, atcLink);
        return String.format(SLACK_TEMPLATE, atcLink, cyberLink, tksLink, dasheLink);
    }

    public static String genShopifyDiscordRandom(String url){
        String cyberLink = String.format(CYBER_LINK, url);
        String tksLink = String.format(TKS_LINK, url);
        String dasheLink = String.format(DASHE_LINK, url);
        return String.format(DISCORD_RANDOM_TEMPLATE, cyberLink, tksLink, dasheLink);
    }

    public static String genShopifySlackRandom(String url){
        String cyberLink = String.format(CYBER_LINK, url);
        String tksLink = String.format(TKS_LINK, url);
        String dasheLink = String.format(DASHE_LINK, url);
        return String.format(SLACK_RANDOM_TEMPLATE, cyberLink, tksLink, dasheLink);
    }

    private static String getWhatLink(String url, String variant){
        try{
            URL url1 = new URL(url);
            return url1.getHost() + url1.getPath() + (variant != null ? "?variant=" + variant : "");
        } catch(Exception e){
            return "";
        }
    }
}
