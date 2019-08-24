package com.restocktime.monitor.util.httprequests.headers;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import okhttp3.Request;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class GoogleChromeHeaderDecorator implements HeaderDecorator {
    private String userAgent;



    /**
     * {@inheritDoc}
     */
    public Request.Builder decorateHeaders(Request.Builder builder) {
        return builder
                .header("cache-control", "max-age=0")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", userAgent)
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .addHeader("sec-fetch-site", "none")
                .addHeader("accept-language", "en-US,en;q=0.9");
    }


}
