package com.restocktime.monitor.helper.httprequests.model;

import org.apache.http.Header;

import java.util.List;

public class BasicHttpResponse {
    private String body;
    private int responseCode;
    private List<Header> headers;

    public BasicHttpResponse(String body, int responseCode) {
        this.body = body;
        this.responseCode = responseCode;
    }

    public BasicHttpResponse(String body, int responseCode, List<Header> headers) {
        this.body = body;
        this.responseCode = responseCode;
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }
}
