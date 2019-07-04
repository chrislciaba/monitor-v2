package com.restocktime.monitor.notifications.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.notifications.model.discord.DiscordMessage;
import com.restocktime.monitor.notifications.model.discord.Embed;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class Discord {

    final static Logger logger = Logger.getLogger(Discord.class);

    private List<Embed> embeds;
    private String webhookUrl;
    private ObjectMapper objectMapper;
    private List<String> messages;
    private final int MAX_ATTACHMENTS = 20;
    private DiscordLog discordLog;

    public Discord(String webhookUrl, List<Embed> embeds, List<String> messages) {
        this.webhookUrl = webhookUrl;
        this.embeds = embeds;
        this.messages = messages;
        objectMapper = new ObjectMapper();
        this.discordLog = new DiscordLog(Discord.class);
    }

    public void run(){

        if(messages != null){
            for(String message : messages) {
                sendToDiscord(null, message, webhookUrl);
            }
        }

        if(embeds != null) {
            for (int i = 0; i < (embeds.size() / MAX_ATTACHMENTS + 1); i++) {
                List<Embed> embedList = embeds.subList(i * MAX_ATTACHMENTS,
                        Math.min(embeds.size(), (i + 1) * MAX_ATTACHMENTS));
                sendToDiscord(embedList, null, webhookUrl);
            }
        }

    }

    public void sendToDiscord(List<Embed> embeds, String message, String webhookUrl){
        DiscordMessage discordMessage = new DiscordMessage(embeds, message);
        StringEntity stringEntity;
        String s;
        try {
            s = objectMapper.writeValueAsString(discordMessage).replaceAll("[^\\x00-\\x7F\\s]", "");
            logger.info(s);
            if(s.contains("RestockTime x Endurance"))
                Timeout.timeout(9000);
            stringEntity = new StringEntity(s);

        } catch (Exception e) {

            return;
        }
        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        HttpPost httpPost = new HttpPost(webhookUrl);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(stringEntity);
        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode() >= 400){
                logger.error(EntityUtils.toString(httpResponse.getEntity()));
                discordLog.debug(httpResponse.getStatusLine().getStatusCode() + " response received when sending " + s);
            }

        } catch (Exception e) {

        } finally {
            httpPost.releaseConnection();
        }

    }
}