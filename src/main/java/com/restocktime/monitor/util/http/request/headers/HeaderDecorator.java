package com.restocktime.monitor.util.http.request.headers;

import okhttp3.Request;

public interface HeaderDecorator {
    /**
     * Decorates an okhttp request with headers
     * @param builder RequestBuilder
     * @return request builder decorated with headers
     */
    Request.Builder decorateHeaders(Request.Builder builder);
}
