package com.restocktime.monitor.monitors.parse.solebox;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.keywords.KeywordSearchHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoleboxProductPageResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SoleboxProductPageResponseParser.class);
    private KeywordSearchHelper keywordSearchHelper;
    Map<String, String> prevUrls;
    private List<String> formatNames;

    Pattern productPattern = Pattern.compile("<a id=\"\" href=\"([^\"]*)\" class=\"fn\" title=\"[^\"]*\"><div class=\"gridPicture\"><img src=\"([^\"]*)\"\\s+alt=\"([^\"]*)\">");

    public SoleboxProductPageResponseParser(KeywordSearchHelper keywordSearchHelper, List<String> formatNames) {
        this.keywordSearchHelper = keywordSearchHelper;
        prevUrls = new HashMap<>();
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get().replaceAll(">\\s+<", "><");

        Matcher productMatcher = productPattern.matcher(responseString);

        if(responseString.contains("429 - too many requests") || responseString.contains("too many requests") || responseString.contains("leider zu viele anfragen")) {
            logger.info("Banned");
        } else if(basicHttpResponse.getResponseCode().get() >= 400){
            logger.info("Solebox error code " + basicHttpResponse.getResponseCode());
        } else {
            List<String> urls = new ArrayList<>();
            while (productMatcher.find()) {

                String name = productMatcher.group(3);
                String url = productMatcher.group(1);

                if(url.contains("?")){
                    url = url.split("\\?")[0];
                }

                if (keywordSearchHelper.search(name) && !prevUrls.containsKey(url) && !isFirst) {
                    String img = productMatcher.group(2);
                    DefaultBuilder.buildAttachments(attachmentCreater, url, img, "Solebox", name, formatNames);
                }
                urls.add(url);

            }
            if(urls.size() > 0) {
                prevUrls.clear();
                logger.info("URLS found " + urls.size());
                for (String url : urls) {
                    logger.info(url);
                    prevUrls.put(url, "");
                }
            }
        }
    }
}
