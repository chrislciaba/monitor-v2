package com.restocktime.monitor.util.http.client.builder.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Builder
@Getter
@Setter
public class BasicRequestClient {
    private Optional<CloseableHttpClient> closeableHttpClient;
    private RequestConfig requestConfig;
    private List<Header> headerList;
    private Optional<OkHttpClientConfig> okHttpClientConfig;
    private CookieStore cookieStore;
    private HttpHost httpHost;
    private RequestConfig noRedirectrequestConfig;


    public Optional<OkHttpClient> getOkHttpClient(ConnectionPool connectionPool) {
        OkHttpClientConfig okHttpClientConfig = this.okHttpClientConfig.get();
        CookieJar cookieJar = new CookieJar() {
            private final HashMap<String, Map<String, Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                Map<String, Cookie> cookieMap = okHttpClientConfig.getCookies();
                for (Cookie cookie : cookies) {
                    cookieMap.put(cookie.name(), cookie);
                }

                cookieStore.put(url.host(), cookieMap);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {

                List<Cookie> cookies = new ArrayList<>();
                if(cookieStore.get(url.host()) == null)
                    return cookies;
                for(String key : cookieStore.get(url.host()).keySet()) {
                    cookies.add(cookieStore.get(url.host()).get(key));
                }

                return cookies;
            }
        };

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .connectionPool(
                        connectionPool
                )
                .retryOnConnectionFailure(false)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(okHttpClientConfig.getProxyHost(), okHttpClientConfig.getProxyPort())));

        if(okHttpClientConfig.getUsername() != null && okHttpClientConfig.getPassword() != null) {
            Authenticator proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credential = Credentials.basic(okHttpClientConfig.getUsername(), okHttpClientConfig.getPassword());
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };

            okHttpClientBuilder.proxyAuthenticator(proxyAuthenticator);
        }

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        return Optional.of(okHttpClient);

    }
}
