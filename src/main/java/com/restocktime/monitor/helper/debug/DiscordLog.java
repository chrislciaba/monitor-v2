package com.restocktime.monitor.helper.debug;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class DiscordLog {
    private String className;

    public DiscordLog(Class clazz) {
        className = clazz.getName();
    }
    public DiscordLog(String className) {
        this.className = className;
    }


    public void debug(String message){
        log(className + ": " + message, "https://discordapp.com/api/webhooks/556905679718318090/WR2gBzHejx4hScDMHXr06CQYQY8xLOK3qyWlNtBgeSvGaYdNKIhK_cmrYNqWyN1-LvUE");
    }

    public void info(String message){
        log(className + ": " + message, "https://discordapp.com/api/webhooks/557632661859074058/uJqNTq-_eGAtKeoDynkiPy7Gc1tg3-qKUsIxWul_eirGV6dTG3ZN6K5PlLRG86-UGWUG");
    }

    public void error(String message){
        log(className + ": " + message, "https://discordapp.com/api/webhooks/557631873174601738/NuNsUSx5UfeRkiIpATDA0L6kFUGVwbc4KYnn9b90m2wZNoa79Ax9fXTC_BqwyyCsH0wd");
    }

    private void log(String message, String url){

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity("{\"content\":\"" + message + "\"}"));
            httpClient.execute(httpPost);
        } catch (Exception e) {

        }
    }
}
