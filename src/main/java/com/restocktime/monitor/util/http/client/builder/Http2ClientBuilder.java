package com.restocktime.monitor.util.http.client.builder;


import com.restocktime.monitor.util.http.client.builder.model.HttpProxy;
import okhttp3.*;

import java.util.ArrayList;
import java.util.List;

public class Http2ClientBuilder {

    public List<OkHttpClient> buildClients(String url, List<String> proxyList, String site) throws Exception{
        List<OkHttpClient> okHttpClients = new ArrayList<>();

        for (String proxyStr : proxyList) {
            /*// Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            int proxyPort = 30011;
            String proxyHost = "gr.smartproxy.com";
            String username = "meshclaw";
            String password = "whiteclaw";

            Authenticator proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credential = Credentials.basic(username, password);
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };

            okHttpClients.add(new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                    .proxyAuthenticator(proxyAuthenticator)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build()
            );*/

        }

        return okHttpClients;

    }

    private HttpProxy transformToProxy(String proxyStr){
        String proxyParts[] = proxyStr.trim().split(":");
        if(proxyParts.length == 4) {
            return new HttpProxy(proxyParts[0], Integer.parseInt(proxyParts[1]), proxyParts[2], proxyParts[3]);

        } else if(proxyParts.length == 2){
            return new HttpProxy(proxyParts[0], Integer.parseInt(proxyParts[1]), null, null);
        }

        return null;
    }

}
