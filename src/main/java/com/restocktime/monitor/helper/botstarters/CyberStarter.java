package com.restocktime.monitor.helper.botstarters;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class CyberStarter {
    private static final String URL_TEMPLATE = "https://cybersole.io/api/command?quicktask=%s";

    public static void startQuickTask(String url, String cookie){
        String qtLink =  String.format(URL_TEMPLATE, url);
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(qtLink);
        httpGet.setHeader("Cookie", "session=" + cookie);
        try {
            httpClient.execute(httpGet);
        } catch (Exception e){}
    }
}
