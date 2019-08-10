package com.restocktime.monitor.util.password;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordHelper {
    public static boolean getPassStatus(
            AttachmentCreater attachmentCreater,
            BasicHttpResponse basicHttpResponse,
            String url,
            boolean isFirst,
            boolean isPassUp,
            List<String> formatNames
    ){
        if(!isPassUp && (basicHttpResponse.getBody().get().contains("password") || basicHttpResponse.getBody().get().contains("Password"))){
            if(!isFirst) {
                DefaultBuilder.buildAttachments(attachmentCreater, UrlHelper.deriveBaseUrl(url), null, UrlHelper.getHost(url), "PASSWORD UP", formatNames);
                if(url.contains("yeezy")){
                    sentText("PASSWORD PAGE UP " + UrlHelper.deriveBaseUrl(url));
                }

            }

            return true;
        } else if(isPassUp && basicHttpResponse.getResponseCode().get() == 200 && basicHttpResponse.getBody().get().contains("/products/")){

            if(!isFirst)
                DefaultBuilder.buildAttachments(attachmentCreater,  UrlHelper.deriveBaseUrl(url), null, UrlHelper.getHost(url), "PASSWORD DOWN", formatNames);
            if(url.contains("yeezy")){
                sentText("PASSWORD PAGE DOWN " + UrlHelper.deriveBaseUrl(url));
            }
            return false;
        }

        return isPassUp;
    }

    private static void  sentText(String msg){

        BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials("AKIAU2X6KOKCUUJHO3VX","8MHphePHFdLk/5hpE8XsN/Dgsv/Wt12JpclAZe6d");

        AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials)).withRegion(Regions.US_WEST_2).build();
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        //<set SMS attributes>
        String topicArn = "arn:aws:sns:us-west-2:332319584901:RestockTimePrivate";//createSNSTopic(snsClient);
        //<subscribe to topic>
        //sendSMSMessageToTopic(snsClient, topicArn, message, smsAttributes);
        PublishResult result = snsClient.publish(new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(msg)
                .withMessageAttributes(smsAttributes));
    }
}
