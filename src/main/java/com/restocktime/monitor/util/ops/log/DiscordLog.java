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
                s = "https://discordapp.com/api/webhooks/622922205826646036/LlPRMOuNrTjJV_kl4RaGuXzVIRxCwTvHYuphiAXww8WTmKrOZjoCzg-6wjjwxdB4w7d0";
            } else if(webhookType.equals(WebhookType.MESH)){
                s = "https://discordapp.com/api/webhooks/622922547985645588/iehzl5fvV4IXDMqK11H9gdPrjbvijZnrTeYMKuUu1kLK1uIz_NRAT-qWUbnQpL8JwmGr";
            } else  if(webhookType.equals(WebhookType.CF)) {
                s = "https://discordapp.com/api/webhooks/622922338379235328/tC0Bs9gEcWXQ97KPwaqX7SMUdDypcr4GFkh9B01JYK2XFXDpbdbrpYrj5gGNfJVuqHP1";
            } else if(webhookType.equals(WebhookType.SP)){
                s = "https://discordapp.com/api/webhooks/622922919571357697/YBdh92ynelFa_VsDrnO_Zn8XPQWRDbq_N8_fwKbJjtFyiKzf83OXhDJ9BkfSMwj6D0Xp";
            } else if(webhookType.equals(WebhookType.SVD)){
                s = "https://discordapp.com/api/webhooks/622922712154767375/NhTH64w6Fandw9D3V2pTUt7XZ-YRY-7DG-0YQ7fDxzoa1vs8dvDoQD37Q96Ai3BBlcYL";
            } else if(webhookType.equals(WebhookType.BSTN)){
                s = "https://discordapp.com/api/webhooks/622921838904737814/9DnpMIYNw7Vdbv7m-BaDm3X3u8-XdiSYR6DV9C0Nc2dczf2N6jHIz5kOsn0VcgLT0zFi";
            } else if(webhookType.equals(WebhookType.NAKED)){
                s = "https://discordapp.com/api/webhooks/622929166278328351/IPq9lR3Uyjg844jAHRzHaACFvWhLpa-z8Ssbg73snyuexAehtUELHHDaogjXcPBUt4i4";
            } else {
                s = "https://discordapp.com/api/webhooks/622923098475200532/UqjX9b8bCz5RBeS324LrgwKBJG4VkCGZc3PK17tjb1lyBB8QDW5JLfjWhTWdrMIpr4Jx";
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
