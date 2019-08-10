package com.restocktime.monitor.helper.httprequests;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.exception.MonitorRequestException;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;

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
            httpResponse = basicRequestClient.getCloseableHttpClient().execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();
            String resp = EntityUtils.toString(entity);
            EntityUtils.consumeQuietly(entity);
            return new BasicHttpResponse(resp, httpResponse.getStatusLine().getStatusCode(), Arrays.asList(httpResponse.getAllHeaders()));
        } catch(Exception e) {
            log.info(EXCEPTION_LOG_MESSAGE, e);
            throw new MonitorRequestException("get request failed", e);
        } finally {
            httpGet.releaseConnection();
            try {
                httpResponse.close();
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
                httpResponse = basicRequestClient.getCloseableHttpClient().execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();

                return new BasicHttpResponse(EntityUtils.toString(entity), httpResponse.getStatusLine().getStatusCode());
            } catch(Exception e) {
                log.info(EXCEPTION_LOG_MESSAGE, e);
                throw new MonitorRequestException("post request failed", e);
            } finally {
                httpPost.releaseConnection();
                try {
                    httpResponse.close();
                } catch (Exception e){
                    log.info(EXCEPTION_LOG_MESSAGE, e);
                    throw new MonitorRequestException("post request failed on close", e);
                }
            }
    }
}
