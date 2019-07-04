package com.restocktime.monitor.monitors.parse.offwhite.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OffWhiteAllResponseParser {
    final static Logger logger = Logger.getLogger(OffWhiteProductAbstractResponseParser.class);

    private StockTracker stockTracker;
    private KeywordSearchHelper keywordSearchHelper;
    private Pattern imagePattern = Pattern.compile("<img itemProp=\"image\" alt=\"([^\"]*)\" class=\"top\" src=\"([^\"]*)\" /><figcaption><div class='brand-name'>([^<]*)</div>");
    private final String urlTemplate = "<a itemProp=\"url\" href=\"([^\"]*)\"><span content='[^']*%s' itemProp='name' style='display:none'>";
    private List<String> formatNames;

    public OffWhiteAllResponseParser(StockTracker stockTracker, KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.keywordSearchHelper = keywordSearchHelper;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String responseString = basicHttpResponse.getBody().replaceAll(">\\s+<", "><").replaceAll("\n", "");
        if(responseString.contains("<h1>502 Bad Gateway</h1>")){
            logger.info("BANNED PROXY ON OFF WHITE 3");
            return;
        }

        Matcher imageMatcher = imagePattern.matcher(responseString);
        while(imageMatcher.find()){
            String slug = imageMatcher.group(1).split("\\s+")[0];
            String imgUrl = imageMatcher.group(2);
            String name = imageMatcher.group(3);
            //logger.info(name);

            if(keywordSearchHelper.search(name) && stockTracker.notifyForObject(slug, isFirst)){
                Pattern urlPattern = Pattern.compile(String.format(urlTemplate, slug));
                Matcher m = urlPattern.matcher(responseString);
                if(m.find()){
                    DefaultBuilder.buildAttachments(attachmentCreater, "https://www.off---white.com" + m.group(1), imgUrl, "OFF WHITE 3", name, formatNames);
                  //  attachmentCreater.addMessages("https://www.off---white.com" + m.group(1), name, "OFF WHITE 3", null, null, imgUrl);
                }
            }
        }

        if(attachmentCreater.isEmpty()){
            logger.info("Nothing new found");
        }

    }

}
