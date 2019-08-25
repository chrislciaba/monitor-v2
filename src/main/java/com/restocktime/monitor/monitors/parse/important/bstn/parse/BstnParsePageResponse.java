package com.restocktime.monitor.monitors.parse.important.bstn.parse;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.model.discord.DiscordField;
import com.restocktime.monitor.notifications.model.slack.SlackField;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BstnParsePageResponse {
    //<a class="pName" title="Nike - Air VaporMax Flyknit 2" href="/p/nike-air-vapormax-flyknit-2-942843-012-83335">

    final static Logger logger = Logger.getLogger(BSTNParseProductAbstractResponse.class);
    private StockTracker stockTracker;
    private String url;
    private final String productPatternStr = "<a class=\"pName\" title=\"([^\"]*)\" href=\"([^\"]*)\">";
    private final String COMING_SOON_TEMPLATE = "<a class=\"plink image\" href=\"%s\"><div class=\"pLabel comingsoon\">";
    private final String SIZE = "<li title=\"\"><a href=\"(%s-[0-9]*)\" EU='([0-9,]*)' US='([0-9,]*)'>";
    private final String ALL_SIZES = "<a href=\"(%s-[0-9]*)\" EU='([0-9,]*)' US='([0-9,]*)'>";

    private KeywordSearchHelper keywordSearchHelper;
    private Pattern pattern;
    private List<String> formatNames;


    public BstnParsePageResponse(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, String url, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        pattern = Pattern.compile(productPatternStr);
        this.keywordSearchHelper = keywordSearchHelper;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");
        Matcher productMatcher = pattern.matcher(responseString);
        boolean found = false, isComingSoon;
        while(productMatcher.find()){
            if(keywordSearchHelper.search(productMatcher.group(1))){
                found = true;
                String link = productMatcher.group(2);
                String title = productMatcher.group(1).toUpperCase();
                String key = link;
                isComingSoon = false;
                if(responseString.contains(String.format(COMING_SOON_TEMPLATE, link))){
                    key = link + "comingsoon";
                    isComingSoon = true;

                }

                List<SlackField> slackSlackFields = null;
                List<DiscordField> discordDiscordFields = null;

                if(stockTracker.notifyForObject(key, isFirst)) {

                    String sizes = null;
                    if(!isComingSoon){
                        slackSlackFields = new ArrayList<>();
                        discordDiscordFields = new ArrayList<>();
                        Pattern p = Pattern.compile(String.format(SIZE, link));
                        Matcher m = p.matcher(responseString);

                        Pattern p1 = Pattern.compile(String.format(ALL_SIZES, link));
                        Matcher m1 = p1.matcher(responseString);
                        List<String> sizelist = new ArrayList<>();
                        Map<String, Boolean> inStockLinks = new HashMap<>();
                        List<String> allLinks = new ArrayList<>();

                        while(m.find()){
                            sizelist.add(m.group(3));
                            inStockLinks.put(m.group(1), true);
                        }

                        while(m1.find()){
                            if(!inStockLinks.containsKey(m1.group(1))){
                                stockTracker.setOOS(m1.group(1));
                            }
                        }

                        if(!sizelist.isEmpty()) {
                            sizes = String.join(" / ", sizelist);
                            slackSlackFields.add(new SlackField("Available Sizes", sizes));
                            discordDiscordFields.add(new DiscordField("Available Sizes", sizes, false));

                        }
                    }

                    DefaultBuilder.buildAttachments(attachmentCreater, "https://bstn.com" + link, null, "BSTN", title, formatNames);

                  //  attachmentCreater.addMessages("https://bstn.com" + link, title,"BSTN", slackSlackFields, discordDiscordFields);
                } else if(slackSlackFields != null && slackSlackFields.isEmpty()){
                    stockTracker.setOOS(link);
                }
            }
        }

        if(!found && !responseString.contains("maximum number")){
            logger.info("no new products");
        } else if(responseString.contains("maximum number")){
            logger.info("bstn max number");
        } else if(found) {
            logger.info("found");
        }
    }
}
