package com.restocktime.monitor.monitors.parse.shopify.attachment;

import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.Variant;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SolefoodBuilder {
    private static final String CART_LINK = "%s/cart/%s:1";
    private static final String CYBER_LINK = "https://cybersole.io/dashboard/quicktask?url=%s";
    //private static final String WOP_LINK = "http://client.wopbot.com/createTask?url=%s";
    private static final String WHAT_LINK = "https://whatbot.club/redirect-qt?qt=whatbot://%s";
    //private static final String PD_LINK = "https://restocktime-redirect.herokuapp.com/redirect?q=destroyer://%s";
    private static final String EVE_LINK = "https://restocktime-redirect.herokuapp.com/redirect?eveLink=%s";
    private static final String DASHE_LINK = "https://api.dashe.io/v1/actions/quicktask?url=%s";
    //private static final String SOLE_LINK = "http://localhost:4444/?url=%s";
    private static final String TKS_LINK = "https://thekickstationapi.com/quick-task.php?link=%s&autostart=true";
    private static final String TKS_ATC_LINK = "https://thekickstationapi.com/quick-task.php?link=%s&autostart=true&isatclink=true";


    static final String DISCORD_TEMPLATE = "[ATC](%s) / [Cyber](%s) / [TKS](%s) / [Dashe](%s) / [What](%s)";
    static final String DISCORD_RANDOM_TEMPLATE = "[Cyber](%s) / [TKS](%s) / [Dashe](%s) / [What](%s)";
    static final String SLACK_TEMPLATE = "<%s|ATC> / <%s|Cyber> / <%s|TKS> / <%s|Dashe> / <%s|What>";
    static final String SLACK_RANDOM_TEMPLATE = "<%s|Cyber> / <%s|TKS> / <%s|Dashe> / <%s|What>";

    public static void addAttachment(AttachmentCreater attachmentCreater, String url, String imgUrl, String name, String price, List<Variant> variants, int num, String key) {
        List<SlackField> slackFields = new ArrayList<>();
        List<DiscordField> discordFields = new ArrayList<>();

        for (Variant v : variants) {
            if (v.getAvailable()) {
                slackFields.add(new SlackField(v.getTitle(), genShopifySlackWithSizes(url, v.getId()), false));
                discordFields.add(new DiscordField(v.getTitle(), genShopifyDiscordWithSize(url, v.getId()), false));
            }
        }

        slackFields.add(new SlackField("Price (Approximate):", price, false));
        slackFields.add(new SlackField("Random Size", genShopifySlackRandom(url)));
        discordFields.add(new DiscordField("Price (Approximate):", price, false));
        discordFields.add(new DiscordField("Random Size", genShopifyDiscordRandom(url), false));
        attachmentCreater.addMessages(url, "", name, "Shopify " + num + " (" + UrlHelper.getHost(url) + ")", slackFields, discordFields, imgUrl, null, key);

    }

    public static String genShopifyDiscordWithSize(String url, String variant){
        String atcLink = String.format(CART_LINK, UrlHelper.deriveBaseUrl(url), variant);
        String cyberLink = String.format(CYBER_LINK, atcLink);
        String tksLink = String.format(TKS_ATC_LINK, atcLink);
        String dasheLink = String.format(DASHE_LINK, atcLink);
        //String wopLink = String.format(WOP_LINK, atcLink);
        // String pdLink = String.format(PD_LINK, atcLink);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, variant));
        //String soleLink = String.format(SOLE_LINK, atcLink);
        return String.format(DISCORD_TEMPLATE, atcLink, cyberLink, tksLink, dasheLink, whatLink);
    }

    public static String genShopifySlackWithSizes(String url, String variant){
        String atcLink = String.format(CART_LINK, UrlHelper.deriveBaseUrl(url), variant);
        String cyberLink = String.format(CYBER_LINK, atcLink);
        String tksLink = String.format(TKS_ATC_LINK, atcLink);
        String dasheLink = String.format(DASHE_LINK, atcLink);
        // String wopLink = String.format(WOP_LINK, atcLink);
        // String pdLink = String.format(PD_LINK, atcLink);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, variant));
        // String soleLink = String.format(SOLE_LINK, atcLink);
        return String.format(SLACK_TEMPLATE, atcLink, cyberLink, tksLink, dasheLink, whatLink);
    }

    public static String genShopifyDiscordRandom(String url){
        String cyberLink = String.format(CYBER_LINK, url);
        String tksLink = String.format(TKS_LINK, url);
        String dasheLink = String.format(DASHE_LINK, url);
        //String wopLink = String.format(WOP_LINK, url);
        //String pdLink = String.format(PD_LINK, url);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, null));
        return String.format(DISCORD_RANDOM_TEMPLATE, cyberLink, tksLink, dasheLink, whatLink);
    }

    public static String genShopifySlackRandom(String url){
        String cyberLink = String.format(CYBER_LINK, url);
        String tksLink = String.format(TKS_LINK, url);
        String dasheLink = String.format(DASHE_LINK, url);
        //String wopLink = String.format(WOP_LINK, url);
        // String pdLink = String.format(PD_LINK, url);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, null));
        return String.format(SLACK_RANDOM_TEMPLATE, cyberLink, tksLink, dasheLink, whatLink);
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
