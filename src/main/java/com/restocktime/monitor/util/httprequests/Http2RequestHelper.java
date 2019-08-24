package com.restocktime.monitor.util.httprequests;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.headers.HeaderDecorator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.httprequests.model.ResponseErrors;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
public class Http2RequestHelper extends AbstractHttpRequestHelper {
    private HeaderDecorator headerDecorator;


    public BasicHttpResponse performGet(BasicRequestClient basicRequestClient, String url) {
        Request.Builder requestBuilder = new Request.Builder()
                        .url(url);

        requestBuilder = headerDecorator.decorateHeaders(requestBuilder);

        Request request = requestBuilder.build();



        OkHttpClient client = basicRequestClient.getOkHttpClient().get();

        try (Response response = client.newCall(request).execute()) {
            BasicHttpResponse basicHttpResponse = BasicHttpResponse.builder()
                    .body(
                            Optional.of(response.body().string())
                    ).responseCode(
                            Optional.of(response.code()))
                    .error(Optional.empty())
                    .headers(Optional.empty())
                    .build();
            response.body().close();
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            return basicHttpResponse;
        } catch (IOException e) {
            return BasicHttpResponse.builder()
                    .headers(Optional.empty())
                    .error(Optional.of(ResponseErrors.CONNECTION_TIMEOUT))
                    .responseCode(Optional.empty())
                    .body(Optional.empty())
                    .build();
        }
    }

    public BasicHttpResponse performPost(BasicRequestClient basicRequestClient, String url, String body){
        return null;
    }

}
