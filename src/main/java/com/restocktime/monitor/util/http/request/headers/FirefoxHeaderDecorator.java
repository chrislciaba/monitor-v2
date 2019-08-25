package com.restocktime.monitor.util.http.request.headers;

import lombok.AllArgsConstructor;
import okhttp3.Request;

@AllArgsConstructor
public class FirefoxHeaderDecorator implements HeaderDecorator {
    private String userAgent;
    /**
     * {@inheritDoc}
     */
    public Request.Builder decorateHeaders(Request.Builder builder) {
        return builder
                //.header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0")
                .header("user-agent", userAgent)
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("accept-language", "en-US,en;q=0.5")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("te", "trailers");
    }


}
