package com.restocktime.monitor.util.clientbuilder;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.Methods;
import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncRequester;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http.nio.support.BasicResponseConsumer;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.http2.config.H2Config;
import org.apache.hc.core5.http2.frame.RawFrame;
import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
import org.apache.hc.core5.http2.impl.nio.bootstrap.H2RequesterBootstrap;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.util.Timeout;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class Http2Client {
    public static void s(){
        // Create and start requester
        final H2Config h2Config = H2Config.custom()
                .setPushEnabled(false)
                .build();

        final HttpAsyncRequester requester = H2RequesterBootstrap.bootstrap()
                .setH2Config(h2Config)
                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_2)
                .setStreamListener(new H2StreamListener() {

                    @Override
                    public void onHeaderInput(final HttpConnection connection, final int streamId, final List<? extends Header> headers) {
                        for (int i = 0; i < headers.size(); i++) {
                            System.out.println(connection.getRemoteAddress() + " (" + streamId + ") << " + headers.get(i));
                        }
                    }

                    @Override
                    public void onHeaderOutput(final HttpConnection connection, final int streamId, final List<? extends Header> headers) {
                        for (int i = 0; i < headers.size(); i++) {
                            System.out.println(connection.getRemoteAddress() + " (" + streamId + ") >> " + headers.get(i));
                        }
                    }

                    @Override
                    public void onFrameInput(final HttpConnection connection, final int streamId, final RawFrame frame) {
                    }

                    @Override
                    public void onFrameOutput(final HttpConnection connection, final int streamId, final RawFrame frame) {
                    }

                    @Override
                    public void onInputFlowControl(final HttpConnection connection, final int streamId, final int delta, final int actualSize) {
                    }

                    @Override
                    public void onOutputFlowControl(final HttpConnection connection, final int streamId, final int delta, final int actualSize) {
                    }

                })
                .create();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("HTTP requester shutting down");
                requester.close(CloseMode.GRACEFUL);
            }
        });
        requester.start();
        final HttpHost target = new HttpHost("nghttp2.org");
        final String[] requestUris = new String[] {"/httpbin/ip", "/httpbin/user-agent", "/httpbin/headers"};

        final CountDownLatch latch = new CountDownLatch(requestUris.length);
        for (final String requestUri: requestUris) {
            try {
                final Future<AsyncClientEndpoint> future = requester.connect(target, Timeout.ofSeconds(5));
                final AsyncClientEndpoint clientEndpoint = future.get();
                clientEndpoint.execute(
                        new BasicRequestProducer(Methods.GET, target, requestUri),
                        new BasicResponseConsumer<>(new StringAsyncEntityConsumer()),
                        new FutureCallback<Message<HttpResponse, String>>() {

                            @Override
                            public void completed(final Message<HttpResponse, String> message) {
                                clientEndpoint.releaseAndReuse();
                                final HttpResponse response = message.getHead();
                                final String body = message.getBody();
                                System.out.println(requestUri + "->" + response.getCode());
                                System.out.println(body);
                                latch.countDown();
                            }

                            @Override
                            public void failed(final Exception ex) {
                                clientEndpoint.releaseAndDiscard();
                                System.out.println(requestUri + "->" + ex);
                                latch.countDown();
                            }

                            @Override
                            public void cancelled() {
                                clientEndpoint.releaseAndDiscard();
                                System.out.println(requestUri + " cancelled");
                                latch.countDown();
                            }

                        });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        try {
            latch.await();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Shutting down I/O reactor");
        requester.initiateShutdown();

    }
}
