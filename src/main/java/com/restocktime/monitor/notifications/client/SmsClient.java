package com.restocktime.monitor.notifications.client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;

public class SmsClient {
    private AmazonSNS snsClient;

    public SmsClient(String accessKey, String secretKey, Map<String, String> regionKeys){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAJUIB64FE7N32NNKQ", "FK83NAOrP0kPqfF3EVp/3kBBiCWfqKuJw0ySJ05s");
        this.snsClient = AmazonSNSClient.builder().withRegion("us-west-2").withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
    }

    public  void sendMessage(String message, String region){

        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        String topicArn;
        if(region.equals("US")){
            topicArn = "arn:aws:sns:us-west-2:332319584901:RestockTime";
        } else if(region.equals("UK")) {
            topicArn = "arn:aws:sns:us-west-2:332319584901:RestockTime-UK";
        } else {
            return;
        }

        sendSMSMessageToTopic(snsClient, topicArn, message, smsAttributes);
    }

    public void sendSMSMessageToTopic(AmazonSNS snsClient, String topicArn,
                                             String message, Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(message)
                .withMessageAttributes(smsAttributes));
    }
}
