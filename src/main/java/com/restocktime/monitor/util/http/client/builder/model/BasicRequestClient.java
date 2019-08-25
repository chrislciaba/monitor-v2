package com.restocktime.monitor.util.http.client.builder.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;
import java.util.Optional;

@Builder
@Getter
@Setter
public class BasicRequestClient {
    private Optional<CloseableHttpClient> closeableHttpClient;
    private RequestConfig requestConfig;
    private List<Header> headerList;
    private Optional<OkHttpClient> okHttpClient;
    private CookieStore cookieStore;
    private HttpHost httpHost;
    private RequestConfig noRedirectrequestConfig;
}
