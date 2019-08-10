package com.restocktime.monitor.util.httprequests;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;

public abstract class AbstractHttpRequestHelper {
    public abstract BasicHttpResponse performGet(BasicRequestClient basicRequestClient, String url);

    public abstract BasicHttpResponse performPost(BasicRequestClient basicRequestClient, String url, String body);
}
