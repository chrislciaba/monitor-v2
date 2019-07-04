package com.restocktime.monitor.helper.clientbuilder.model;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

public class BasicRequestClient {
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private List<Header> headerList;
   // private RequestContext requestContext;
    private CookieStore cookieStore;
    private HttpHost httpHost;
    private RequestConfig noRedirectrequestConfig;


    public BasicRequestClient(CloseableHttpClient closeableHttpClient, RequestConfig requestConfig, List<Header> headerList, Object requestContext, CookieStore cookieStore, HttpHost httpHost, RequestConfig noRedirectrequestConfig) {
        this.closeableHttpClient = closeableHttpClient;
        this.requestConfig = requestConfig;
        this.headerList = headerList;
       // this.requestContext = requestContext;
        this.cookieStore = cookieStore;
        this.httpHost = httpHost;
        this.noRedirectrequestConfig = noRedirectrequestConfig;
    }

    public CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public RequestConfig getNoRedirectrequestConfigRequestConfig() {
        return noRedirectrequestConfig;
    }

    public List<Header> getHeaderList() {
        return headerList;
    }

   /* public RequestContext getRequestContext() {
        return requestContext;
    }*/

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public HttpHost getHttpHost() {
        return httpHost;
    }

    public void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
        this.closeableHttpClient = closeableHttpClient;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setHeaderList(List<Header> headerList) {
        this.headerList = headerList;
    }

   /* public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }*/

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public void setHttpHost(HttpHost httpHost) {
        this.httpHost = httpHost;
    }
}
