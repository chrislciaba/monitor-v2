package com.restocktime.monitor.util.http.client.builder.model;

import lombok.Builder;
import lombok.Getter;
import okhttp3.ConnectionPool;
import okhttp3.Cookie;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@Builder
public class OkHttpClientConfig {
    private String proxyHost;
    private Integer proxyPort;
    private String username;
    private String password;
    private Map<String, Cookie> cookies;
    private ConnectionPool connectionPool;
}
