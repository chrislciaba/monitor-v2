package com.restocktime.monitor.monitors.parse.twitter.parse;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.twitter.attachment.DefaultTwitter;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterAbstractResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(TwitterAbstractResponseParser.class);
    private StockTracker stockTracker;
    private String url;
    private KeywordSearchHelper keywordSearchHelper;
    private final String namePatternStr = "<div class=\"tweet-text\" data-id=\"([^\"]*)\">\\s*<div class=\"dir-ltr\" dir=\"ltr\">(.*)\\s*</div";
    private Pattern cyberPattern = Pattern.compile("https://cybersole\\.io[^)]*password=[^)]*");
    private Pattern bioPattern = Pattern.compile("<div class=\"bio\"><div class=\"dir-ltr\" dir=\"ltr\">\\s*(.*)\\s*</div></div>");
    private List<String> formatNames;

    //private final Pattern linkPattern = Pattern.compile("data-url=\"([^\"]*)\"class=\"twitter_external_link")

    private Pattern pattern;

    public TwitterAbstractResponseParser(StockTracker stockTracker, String url, KeywordSearchHelper keywordSearchHelper, List<String> formatNames){
        this.stockTracker = stockTracker;
        this.url = url;
        this.keywordSearchHelper = keywordSearchHelper;
        pattern = Pattern.compile(namePatternStr);
        this.formatNames = formatNames;

    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        Matcher bioMatcher = bioPattern.matcher(responseString.replaceAll(">\\s+<", "><"));

        if(bioMatcher.find()){
            String bio = bioMatcher.group(1).replaceAll("<a[^>]*data-expanded-url=\"([^\"]*)\"class=\"twitter-timeline-link\"[^>]*>[^>]*</a>", "($1)");
            if(stockTracker.notifyForObject(bio, isFirst)){
                if(keywordSearchHelper.search(bio)) {
                    //attachmentCreater.addMessages("", "Bio update from " + url.substring(20), bio, null, null);
                   // attachmentCreater.addTextMessage();
                }



                Matcher cyberMatcher = cyberPattern.matcher(bio);
                if(cyberMatcher.find()){
                    String cyberLink = cyberMatcher.group(1);
                    logger.info(bio);

                    attachmentCreater.addTextMessage("@everyone POTENTIAL CYBER RESTOCK LINK FOUND IN BIO OF " + url.substring(20) + ": " + cyberLink);
                }
            }
        }



        Matcher m = pattern.matcher(responseString);


        while(m.find()){
            String text = m.group(2);
            //logger.info(text);
            text = text.replaceAll("<a[^>]*data-url=\"([^\"]*)\"class=\"twitter_external_link[^>]*>[^>]*</a>", "($1)").replaceAll("<a[^>]*>([^>]*)</a>", "$1").replaceAll("&#[A-Za-z0-9]*;", " ").replaceAll("\\s+", " ").trim();
            String link = url + "/status/" + m.group(1);
            if(stockTracker.notifyForObject(link, isFirst)){

                if(keywordSearchHelper.search(text)) {
                  //  logger.info(text);
                   // attachmentCreater.addMessages(link, url.substring(20), text, null, null);
                    for(String format : formatNames) {
                        DefaultTwitter.addAttachment(attachmentCreater, link, text, url.substring(20), format);
                    }
                }

                Matcher cyberMatcher = cyberPattern.matcher(text);
                if(cyberMatcher.find()){
                    String cyberLink = cyberMatcher.group(0);
                    attachmentCreater.addTextMessage("@everyone " + cyberLink);
                }
            }
        }


    }
}
