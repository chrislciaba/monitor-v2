package com.restocktime.monitor.util.http.request;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.headers.HeaderDecorator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.http.request.model.ResponseErrors;
import com.restocktime.monitor.util.ops.log.DiscordLog;
import com.restocktime.monitor.util.ops.log.WebhookType;
import lombok.AllArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Http2RequestHelper extends AbstractHttpRequestHelper {
    final static Logger logger = Logger.getLogger(Http2RequestHelper.class);

    private HeaderDecorator headerDecorator;


    public BasicHttpResponse performGet(BasicRequestClient basicRequestClient, String url) {
        Request.Builder requestBuilder = new Request.Builder()
                        .url(url);

        requestBuilder = headerDecorator.decorateHeaders(requestBuilder);

        Request request = requestBuilder.build();
        ScheduledExecutorService executor
                = Executors.newScheduledThreadPool(1);
        long startNanos = System.nanoTime();
        OkHttpClient client = basicRequestClient.getOkHttpClient().get();
        Call call = client.newCall(request);

        executor.schedule(() -> {
            logger.info("Canceling call: "
                    + (System.nanoTime() - startNanos) / 1e9f);

            call.cancel();

            logger.info("Canceled call: "
                    + (System.nanoTime() - startNanos) / 1e9f);

        }, 5, TimeUnit.SECONDS);
        try (Response response = call.execute()) {
            BasicHttpResponse basicHttpResponse = BasicHttpResponse.builder()
                    .body(
                            Optional.of(response.body().string())
                    ).responseCode(
                            Optional.of(response.code()))
                    .error(Optional.empty())
                    .headers(Optional.empty())
                    .build();
            //response.body().close();
            //client.dispatcher().executorService().shutdown();
            //client.connectionPool().evictAll();
            return basicHttpResponse;
        } catch (IOException e) {
            return BasicHttpResponse.builder()
                    .headers(Optional.empty())
                    .error(Optional.of(ResponseErrors.CONNECTION_TIMEOUT))
                    .responseCode(Optional.empty())
                    .body(Optional.empty())
                    .build();
        } catch (Exception e) {
            DiscordLog.log(WebhookType.OTHER, e.getMessage());
            return BasicHttpResponse.builder()
                    .headers(Optional.empty())
                    .error(Optional.of(ResponseErrors.UNKNOWN))
                    .responseCode(Optional.empty())
                    .body(Optional.empty())
                    .build();
        }
    }

    public BasicHttpResponse performPost(BasicRequestClient basicRequestClient, String url, String body){
        return null;
    }

}
