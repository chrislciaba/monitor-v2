package com.restocktime.monitor.util.http.request;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

public abstract class AbstractHttpRequestHelper {
    public abstract BasicHttpResponse performGet(BasicRequestClient basicRequestClient, String url);

    public abstract BasicHttpResponse performPost(BasicRequestClient basicRequestClient, String url, String body);
}
