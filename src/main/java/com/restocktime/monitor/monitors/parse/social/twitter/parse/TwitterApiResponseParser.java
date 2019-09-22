package com.restocktime.monitor.monitors.parse.social.twitter.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.social.twitter.attachment.TwitterApi;
import com.restocktime.monitor.monitors.parse.social.twitter.parse.model.User;
import com.restocktime.monitor.monitors.parse.social.twitter.parse.model.UserResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TwitterApiResponseParser implements AbstractResponseParser {

    private ObjectMapper objectMapper;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private String url;
    private Map<String, String> seenTweets;
    private String name;

    public TwitterApiResponseParser(ObjectMapper objectMapper, KeywordSearchHelper keywordSearchHelper, StockTracker stockTracker, List<String> formatNames, String name, Map<String, String> seenTweets) {
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.name = name;
        this.seenTweets = seenTweets;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            System.out.println(basicHttpResponse.getResponseCode().get());
            return;
        }

        try {
            UserResponse userResponse = objectMapper.readValue(basicHttpResponse.getBody().get(), UserResponse.class);

            for (String key : userResponse.getGlobalObjects().getTweets().keySet()) {
                if (isFirst) {
                    seenTweets.put(key, "");
                    continue;
                } else if (!seenTweets.containsKey(key)) {
                    List<User> users = userResponse
                            .getGlobalObjects()
                            .getTweets()
                            .get(key)
                            .getEntities()
                            .getUser_mentions() != null ?

                            userResponse
                            .getGlobalObjects()
                            .getTweets()
                            .get(key)
                            .getEntities()
                            .getUser_mentions()
                            .stream()
                            .map(
                                    userMention -> userResponse.getGlobalObjects().getUsers().get(userMention.getId_str())
                            )
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()) : new ArrayList<>();

                    users.add(
                            userResponse.getGlobalObjects().getUsers().get(
                                    userResponse.getGlobalObjects().getTweets().get(key).getUser_id_str()
                            )
                    );
                    TwitterApi.addAttachment(
                            attachmentCreater,
                            String.format("https://twitter.com/rst/status/%s", key),
                            userResponse.getGlobalObjects().getTweets().get(key).getFull_text(),
                            name,
                            "restocktime",
                            getImages(userResponse, key),
                            users
                    );
                    seenTweets.put(key,  "");
                }
            }

        } catch (Exception e){
e.printStackTrace();
        }
    }

    private List<String> getImages(UserResponse userResponse, String key) {
        if(userResponse.getGlobalObjects().getTweets().get(key).getEntities().getMedia() == null) {
            return ImmutableList.of();
        }
        return userResponse.getGlobalObjects().getTweets().get(key).getEntities().getMedia().stream()
                .map(
                        img -> img.getMedia_url()
                )
                .collect(Collectors.toList());
    }
}
