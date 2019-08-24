package com.restocktime.monitor.util.httprequests.headers;

import okhttp3.Request;

public class SafariHeaderDecorator {

    /**
     * {@inheritDoc}
     */
    public Request.Builder decorateHeaders(Request.Builder builder) {
        return builder
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.1 Safari/605.1.1")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("accept-language", "en-us");
    }
}
