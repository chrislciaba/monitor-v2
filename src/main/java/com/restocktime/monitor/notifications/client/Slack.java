package com.restocktime.monitor.notifications.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.notifications.model.slack.Attachment;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Slack {

    final static Logger logger = Logger.getLogger(Slack.class);

    public static String postLink = "https://slack.com/api/chat.postMessage";

    private SlackObj slackObj;
    private List<Attachment> attachments;
    ObjectMapper objectMapper;
    private final int MAX_ATTACHMENTS = 2;
    List<String> messages;

    public Slack(SlackObj slackObj, List<Attachment> attachments, List<String> messages) {
        this.objectMapper = new ObjectMapper();
        this.slackObj = slackObj;
        this.attachments = attachments;
        this.messages = messages;
    }

    public void run(){

        for(String message : messages){
            send(null, slackObj, message);
        }

        if(attachments != null) {

            for (int i = 0; i < (attachments.size() / MAX_ATTACHMENTS + 1); i++) {
                List<Attachment> attachmentList = attachments.subList(i * MAX_ATTACHMENTS,
                        Math.min(attachments.size(), (i + 1) * MAX_ATTACHMENTS));
                if(attachmentList.size() > 0){
                    send(attachmentList, slackObj, "");
                }
            }
        }
    }

    public void send(List<Attachment> attachmentsToSend, SlackObj slackObj, String message){
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(postLink);
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", slackObj.getBotToken()));
        params.add(new BasicNameValuePair("channel", slackObj.getChannelId()));
        params.add(new BasicNameValuePair("text", message));
        params.add(new BasicNameValuePair("unfurl_links", "true"));
        String attachmentStr;

        if(attachmentsToSend != null) {

            try {
                attachmentStr = objectMapper.writeValueAsString(attachmentsToSend);

            } catch (Exception e) {


                return;
            }

            //logger.info(attachmentStr);


            if (attachmentStr != null) {
                params.add(new BasicNameValuePair("attachments", attachmentStr));
            }
        }


        try {
            //logger.info(attachmentStr);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response =  httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            //logger.info(responseString);
        } catch(Exception e){

        } finally {
            httpPost.releaseConnection();
        }
    }
}
