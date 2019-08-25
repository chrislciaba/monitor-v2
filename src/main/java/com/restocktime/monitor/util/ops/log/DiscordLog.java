package com.restocktime.monitor.util.ops.log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;


public class DiscordLog {

    public static void log(WebhookType webhookType, String message){
        try {
            CloseableHttpClient closeableHttpClient = HttpClients.custom()
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .build();
            String s;
            if(webhookType.equals(WebhookType.SNS)){
                s = "https://discordapp.com/api/webhooks/614995190469361684/SQM-MilzQxztQfXlcXYHVioy1blOyaYCRsKesW3NYGv_tWa1Vb1Uy2lId54e_J4BuyT7";
            } else if(webhookType.equals(WebhookType.MESH)){
                s = "https://discordapp.com/api/webhooks/614995510650077202/HhwvMYPEN1n4VvAj5aTiXBrG8Alm2HkTplUPCYHY4hc5ZWp00xVRpzLvxW1JdnSqYVv6";
            } else  if(webhookType.equals(WebhookType.CF)) {
                s = "https://discordapp.com/api/webhooks/614995736488312844/t9kwBI0v_fgjQBvtYhXC9B5rsIwskAbdmqgKEA1AwHm87TOXq_q56t2538ndETdx9fuT";
            } else {
                s = "https://discordapp.com/api/webhooks/613613241595330561/x9VMO3xUdd-S2p0T-8My-jfm66esNloAv8S98fIzcWKDB47SEGV32mob6nnALuZNM4wH";
            }
            HttpPost httpPost = new HttpPost(s);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity("{\"content\":\"" + message + "\"}"));
            try {
                HttpResponse httpResponse = closeableHttpClient.execute(httpPost);

            } finally {
                httpPost.releaseConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logPrivately(String message){
        try {
            CloseableHttpClient closeableHttpClient = HttpClients.custom()
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .build();
            HttpPost httpPost = new HttpPost("https://discordapp.com/api/webhooks/614716074604363777/jSN7UDYIBqQYqD2DvXBuH5FAtMC3D9f9W47Nv2gC0HDIaJMlIJWYQsP2dwmPWvCylZMd");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity("{\"content\":\"" + message + "\"}"));
            try {
                HttpResponse httpResponse = closeableHttpClient.execute(httpPost);

            } finally {
                httpPost.releaseConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
