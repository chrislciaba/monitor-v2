package com.restocktime.monitor.monitors.parse.social.instagram.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.restocktime.monitor.monitors.parse.social.instagram.attachment.DefaultInstagram;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.social.instagram.model.page.Edge;
import com.restocktime.monitor.monitors.parse.social.instagram.model.page.MainPage;
import com.restocktime.monitor.monitors.parse.social.instagram.model.page.Node;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstagramResponseParser implements AbstractResponseParser {
    private final Pattern jsonPattern = Pattern.compile("<script type=\"text/javascript\">window\\._sharedData = (.*);</script>");

    private ObjectMapper objectMapper;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private String url;


    public InstagramResponseParser(ObjectMapper objectMapper, KeywordSearchHelper keywordSearchHelper, StockTracker stockTracker, List<String> formatNames, String url) {
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        try {

            Matcher m = jsonPattern.matcher(basicHttpResponse.getBody().get());
            if(m.find()) {
                String toParse = m.group(1);


                MainPage mainPage = objectMapper.readValue(toParse.trim(), MainPage.class);
                String bio = mainPage.getEntryData().getProfilePage().get(0).getGraphql().getUser().getBiography();
                String name = mainPage.getEntryData().getProfilePage().get(0).getGraphql().getUser().getFull_name();

                if (stockTracker.notifyForObject(bio, isFirst)) {
                    for (String format : formatNames)
                        DefaultInstagram.addAttachment(attachmentCreater, url, bio, "Bio update: " + name, null, format);
                }

                for (Edge edge : mainPage.getEntryData().getProfilePage().get(0).getGraphql().getUser().getEdge_owner_to_timeline_media().getEdges()) {
                    Node node = edge.getNode();
                    if (stockTracker.notifyForObject(node.getId(), isFirst)) {
                        String imgUrl = node.getDisplayUrl();
                        String text = "";
                        try {
                            text = node.getEdgeMediaToCaption().getEdges().get(0).getNode().getText();
                        } catch (Exception e){

                        }

                        if(Strings.emptyToNull(text) != null && !keywordSearchHelper.search(text)) {
                            continue;
                        }

                        String url = "https://instagram.com/p/" + node.getShortCode();
                        for (String format : formatNames)
                            DefaultInstagram.addAttachment(attachmentCreater, url, text, name, imgUrl, format);
                    }
                }
            }

        } catch (Exception e){

        }
    }
}

