package com.restocktime.monitor.util.log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;


public class DiscordLog {

    public static void log(String message){
        try {
            CloseableHttpClient closeableHttpClient = HttpClients.custom()
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .build();
            HttpPost httpPost = new HttpPost("https://discordapp.com/api/webhooks/613613241595330561/x9VMO3xUdd-S2p0T-8My-jfm66esNloAv8S98fIzcWKDB47SEGV32mob6nnALuZNM4wH");
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
