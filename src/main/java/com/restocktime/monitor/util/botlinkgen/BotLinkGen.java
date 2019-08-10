package com.restocktime.monitor.util.botlinkgen;

import com.restocktime.monitor.util.url.UrlHelper;

import java.net.URL;

public class BotLinkGen {
    private static final String CART_LINK = "%s/cart/%s:1";
    private static final String CYBER_LINK = "https://cybersole.io/dashboard/quicktask?url=%s";
    private static final String WOP_LINK = "http://client.wopbot.com/createTask?url=%s";
    private static final String WHAT_LINK = "https://whatbot.club/redirect-qt?qt=whatbot://%s";
    private static final String SOLE_LINK = "http://localhost:4444/?url=%s";
    private static final String TKS_LINK = "https://thekickstationapi.com/quick-task.php?link=%s&autostart=true";
    private static final String DISCORD_TEMPLATE = "[ATC](%s) / [Cyber](%s) / [WOP](%s) / [What](%s) / [Sole](%s)";
    private static final String DISCORD_RANDOM_TEMPLATE = "[TKS](%s) / [Cyber](%s) / [WOP](%s) / [What](%s)";

    private static final String SLACK_TEMPLATE = "<%s|ATC> / <%s|Cyber> / <%s|WOP> / <%s|What> / <%s|Sole>";
    private static final String SLACK_RANDOM_TEMPLATE = "<%s|TKS> / <%s|Cyber> / <%s|WOP> / <%s|What>";


    private static final String OW_CYBER_LINK = "https://cybersole.io/dashboard/quicktask?url=Off-White:%s";
    private static final String FP_CYBER_LINK = "https://cybersole.io/dashboard/quicktask?url=Footpatrol:%s";
    private static final String OW_DISCORD_TEMPLATE = "[Cyber](%s)";
    private static final String OW_SLACK_TEMPLATE = "<%s|Cyber>";
    private static final String FP_DISCORD_TEMPLATE = "[Cyber](%s)";
    private static final String FP_SLACK_TEMPLATE = "<%s|Cyber>";

    static final String SLACK_ATC_ONLY_TEMPLATE = "<%s|*%s*> (%s)";


    public static String genShopifyAtcSizeAsTitle(String title, String url, String variant, String quantity){
        String atcLink = String.format(CART_LINK, UrlHelper.deriveBaseUrl(url), variant);
        return String.format(SLACK_ATC_ONLY_TEMPLATE, atcLink, title, quantity);
    }


    public static String genShopifyDiscordWithSize(String url, String variant){
        String atcLink = String.format(CART_LINK, UrlHelper.deriveBaseUrl(url), variant);
        String cyberLink = String.format(CYBER_LINK, atcLink);
        String wopLink = String.format(WOP_LINK, atcLink);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, variant));
        String soleLink = String.format(SOLE_LINK, atcLink);
        return String.format(DISCORD_TEMPLATE, atcLink, cyberLink, wopLink, whatLink, soleLink);
    }

    public static String genShopifySlackWithSizes(String url, String variant){
        String atcLink = String.format(CART_LINK, UrlHelper.deriveBaseUrl(url), variant);
        String cyberLink = String.format(CYBER_LINK, atcLink);
        String wopLink = String.format(WOP_LINK, atcLink);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, variant));
        String soleLink = String.format(SOLE_LINK, atcLink);
        return String.format(SLACK_TEMPLATE, atcLink, cyberLink, wopLink, whatLink, soleLink);
    }

    public static String genShopifyDiscordRandom(String url){
        String tksLink = String.format(TKS_LINK, url);
        String cyberLink = String.format(CYBER_LINK, url);
        String wopLink = String.format(WOP_LINK, url);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, null));
        return String.format(DISCORD_RANDOM_TEMPLATE, tksLink, cyberLink, wopLink, whatLink);
    }

    public static String genShopifySlackRandom(String url){
        String tksLink = String.format(TKS_LINK, url);
        String cyberLink = String.format(CYBER_LINK, url);
        String wopLink = String.format(WOP_LINK, url);
        String whatLink = String.format(WHAT_LINK, getWhatLink(url, null));
        return String.format(SLACK_RANDOM_TEMPLATE, tksLink, cyberLink, wopLink, whatLink);
    }

    public static String genOffWhiteSlackLink(String url){
        String cyberLink = String.format(OW_CYBER_LINK, url);
        return String.format(OW_SLACK_TEMPLATE, cyberLink);
    }

    public static String genOffWhiteDiscordLink(String url){
        String cyberLink = String.format(OW_CYBER_LINK, url);
        return String.format(OW_DISCORD_TEMPLATE, cyberLink);
    }

    public static String genFootpatrolSlackLink(String url){
        String cyberLink = String.format(FP_CYBER_LINK, url);
        return String.format(FP_SLACK_TEMPLATE, cyberLink);
    }

    public static String genFootpatrolDiscordLink(String url){
        String cyberLink = String.format(FP_CYBER_LINK, url);
        return String.format(FP_DISCORD_TEMPLATE, cyberLink);
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
