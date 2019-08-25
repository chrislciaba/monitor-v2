package com.restocktime.monitor.util.http.client.builder;

import com.restocktime.monitor.util.http.request.wrapper.CloudflareRequestWrapper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;

import java.io.IOException;

class LoggingInterceptor implements Interceptor {
    final static Logger logger = Logger.getLogger(CloudflareRequestWrapper.class);

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
       logger.debug(request.url() + "\n" + request.headers().toString());

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        logger.debug(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers().toString()));

        return response;
    }
}