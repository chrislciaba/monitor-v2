package com.restocktime.monitor.helper.httprequests;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;

public abstract class AbstractHttpRequestHelper {
    public abstract BasicHttpResponse performGet(BasicRequestClient basicRequestClient, String url);

    public abstract BasicHttpResponse performPost(BasicRequestClient basicRequestClient, String url, String body);
}
