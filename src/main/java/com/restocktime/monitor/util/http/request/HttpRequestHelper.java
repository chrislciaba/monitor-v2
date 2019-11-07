package com.restocktime.monitor.util.http.request;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.http.request.model.ResponseErrors;
import com.restocktime.monitor.util.http.request.exception.MonitorRequestException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Optional;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class HttpRequestHelper extends AbstractHttpRequestHelper {
    final static Logger log = Logger.getLogger(HttpRequestHelper.class);

    public BasicHttpResponse performGet(BasicRequestClient basicRequestClient, String url){
        HttpGet httpGet = new HttpGet(url);

        if(basicRequestClient.getRequestConfig() != null && !url.contains("http://localhost")){
            httpGet.setConfig(basicRequestClient.getRequestConfig());
        }

        if(basicRequestClient.getHeaderList() != null){
            for(Header header : basicRequestClient.getHeaderList()) {
                httpGet.setHeader(header);
            }
        }

        CloseableHttpResponse httpResponse = null;

        try {
            httpResponse = basicRequestClient.getCloseableHttpClient().get().execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();
            String resp = EntityUtils.toString(entity);
            EntityUtils.consumeQuietly(entity);
            return BasicHttpResponse.builder()
                    .body(Optional.of(resp))
                    .responseCode(Optional.of(httpResponse.getStatusLine().getStatusCode()))
                    .error(Optional.empty())
                    .headers(Optional.of(Arrays.asList(httpResponse.getAllHeaders())))
                    .build();

                    //(resp, httpResponse.getStatusLine().getStatusCode(), Arrays.asList(httpResponse.getAllHeaders()));
        } catch (ConnectTimeoutException cte) {
            cte.printStackTrace();
            log.error("Connection timed out");
            return BasicHttpResponse.builder()
                    .body(Optional.empty())
                    .responseCode(Optional.empty())
                    .headers(Optional.empty())
                    .error(Optional.of(ResponseErrors.CONNECTION_TIMEOUT))
                    .build();
        }  catch (SocketTimeoutException ste) {
            ste.printStackTrace();

            log.error("Socket timeout");
            return BasicHttpResponse.builder()
                    .body(Optional.empty())
                    .responseCode(Optional.empty())
                    .headers(Optional.empty())
                    .error(Optional.of(ResponseErrors.CONNECTION_TIMEOUT))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            log.info(EXCEPTION_LOG_MESSAGE, e);
            throw new MonitorRequestException("get request failed", e);
        } finally {
            httpGet.releaseConnection();
            try {
                if(httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e){
                log.error(EXCEPTION_LOG_MESSAGE, e);
                throw new MonitorRequestException("get request failed on close", e);
            }
        }
    }

    public BasicHttpResponse performPost(
            BasicRequestClient basicRequestClient,
            String url,
            String body
    ){
            HttpPost httpPost = new HttpPost(url);
        httpPost.removeHeaders("Referer");
        httpPost.removeHeaders("Origin");



        if(basicRequestClient.getRequestConfig() != null && !url.contains("http://localhost")){
                httpPost.setConfig(basicRequestClient.getRequestConfig());
            }

            if(basicRequestClient.getHeaderList() != null){
                for(Header header : basicRequestClient.getHeaderList()) {
                   // httpPost.setHeader(header);
                    log.info(header.getName());
                }
            }

            CloseableHttpResponse httpResponse = null;
            try {
                httpPost.setEntity(new StringEntity(body));
                httpResponse = basicRequestClient.getCloseableHttpClient().get().execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();

                BasicHttpResponse basicHttpResponse = BasicHttpResponse.builder()
                        .body(Optional.of(EntityUtils.toString(entity)))
                        .responseCode(Optional.of(httpResponse.getStatusLine().getStatusCode()))
                        .error(Optional.empty())
                        .headers(Optional.of(Arrays.asList(httpResponse.getAllHeaders())))
                        .build();
                EntityUtils.consumeQuietly(entity);
                return basicHttpResponse;
            } catch (ConnectTimeoutException cte) {
                log.info("Connection timed out");
                return BasicHttpResponse.builder()
                        .body(Optional.empty())
                        .responseCode(Optional.empty())
                        .headers(Optional.empty())
                        .error(Optional.of(ResponseErrors.CONNECTION_TIMEOUT))
                        .build();
            } catch (SocketTimeoutException ste) {
                log.info("Socket timeout");
                return BasicHttpResponse.builder()
                        .body(Optional.empty())
                        .responseCode(Optional.empty())
                        .headers(Optional.empty())
                        .error(Optional.of(ResponseErrors.CONNECTION_TIMEOUT))
                        .build();
            } catch(Exception e) {
                log.info(EXCEPTION_LOG_MESSAGE, e);
                throw new MonitorRequestException("post request failed", e);
            } finally {
                httpPost.releaseConnection();
                try {
                    if(httpResponse != null) {
                        httpResponse.close();
                    }
                } catch (Exception e){
                    log.info(EXCEPTION_LOG_MESSAGE, e);
                    throw new MonitorRequestException("post request failed on close", e);
                }
            }
    }
}
