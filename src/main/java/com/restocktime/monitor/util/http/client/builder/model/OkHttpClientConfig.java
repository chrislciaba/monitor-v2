package com.restocktime.monitor.util.http.client.builder.model;

import lombok.Builder;
import lombok.Getter;
import okhttp3.Cookie;

import java.util.Map;

@Getter
@Builder
public class OkHttpClientConfig {
    private String proxyHost;
    private Integer proxyPort;
    private String username;
    private String password;
    private Map<String, Cookie> cookies;
}
