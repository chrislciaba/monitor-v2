package com.restocktime.monitor.util.httprequests;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.captcha.TwoCaptchaService;
import com.restocktime.monitor.util.httprequests.model.ResponseErrors;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;
import static org.apache.commons.codec.CharEncoding.UTF_8;

public class CloudflareRequestHelper extends AbstractHttpRequestHelper {

    final static Logger logger = Logger.getLogger(CloudflareRequestHelper.class);
    private final String REGEX_0 = "name=\"pass\" value=\"([^\"]*)\"";
    private final String REGEX_1 = "name=\"jschl_vc\" value=\"([^\"]*)\"";

    private String[] apiKeys;
    private int idx;
    private static final Logger log = Logger.getLogger(CloudflareRequestHelper.class);


    public CloudflareRequestHelper(String[] apiKeys){
        this.apiKeys = apiKeys;
        this.idx = 0;
    }

    private Optional<BasicHttpResponse> doGet(BasicRequestClient basicRequestClient,
                                             String url) {
        HttpGet httpGet = new HttpGet(url);
        if(!url.contains("http://localhost"))
            httpGet.setConfig(basicRequestClient.getRequestConfig());

        if(basicRequestClient.getHeaderList() != null){
            for(Header header : basicRequestClient.getHeaderList()) {
                if(!header.getName().equals("Content-Type"))
                    httpGet.setHeader(header);
            }
        }

        try {

            HttpResponse response = basicRequestClient.getCloseableHttpClient().execute(httpGet);
            HttpEntity entity = response.getEntity();
            int respCode = response.getStatusLine().getStatusCode();

            String responseString = EntityUtils.toString(entity, "UTF-8");
            return Optional.of(
                    BasicHttpResponse.builder()
                            .body(Optional.of(responseString))
                            .responseCode(Optional.of(respCode))
                            .headers(Optional.empty())
                            .error(Optional.empty())
                            .build()
            );
        } catch (ConnectTimeoutException cte) {
            log.info("Connection timed out");
            return Optional.of(
                    BasicHttpResponse.builder()
                            .body(Optional.empty())
                            .responseCode(Optional.empty())
                            .headers(Optional.empty())
                            .error(Optional.of(ResponseErrors.CONNECTION_TIMEOUT))
                            .build());
        } catch (SocketTimeoutException ste) {
            log.info("Socket timeout");
            return Optional.of(
                    BasicHttpResponse.builder()
                            .body(Optional.empty())
                            .responseCode(Optional.empty())
                            .headers(Optional.empty())
                            .error(Optional.of(ResponseErrors.SOCKET_TIMEOUT))
                            .build());
        }
        catch (IOException e) {
            log.error(EXCEPTION_LOG_MESSAGE, e);
            return Optional.empty();
        } finally {
            httpGet.releaseConnection();
        }
    }

    private boolean isBanned(BasicHttpResponse basicHttpResponse, String url, String proxy) {
        if(!basicHttpResponse.getBody().isPresent()){
            return false;
        } if(basicHttpResponse.getBody().get().contains("<title>Access denied")) {
            log.error("banned proxy on sns " + proxy);
            return true;
        } else if(basicHttpResponse.getBody().get().contains("Access denied") || basicHttpResponse.getBody().get().contains("Access Denied")){
            if(basicHttpResponse.getBody().get().toLowerCase().contains("squid")){
                log.error("Squid proxy error for: " + proxy);
            } else {
                log.error("banned proxy on " + UrlHelper.deriveBaseUrl(url) + " " + proxy);
            }
            return true;
        }

        return false;
    }

    private Optional<BasicHttpResponse> checkCapRoute(BasicHttpResponse basicHttpResponse, String url, BasicRequestClient basicRequestClient){
        if(!basicHttpResponse.getBody().isPresent() || !basicHttpResponse.getResponseCode().isPresent()){
            return Optional.empty();
        } else if(basicHttpResponse.getResponseCode().get() != 503){
            if(basicHttpResponse.getResponseCode().get() == 403 && basicHttpResponse.getBody().get().contains("cdn-cgi")){
                BasicHttpResponse capResp = bypassCap(basicRequestClient, url, basicRequestClient.getRequestConfig(), basicHttpResponse.getBody().get(), apiKeys[idx]);
                idx = (idx + 1) % apiKeys.length;
                if(!basicHttpResponse.getBody().get().contains("Checking your browser before accessing")){
                    return Optional.of(capResp);
                } else {
                    if(url.contains("search.jimmyjazz")){
                        return Optional.of(basicHttpResponse);
                    }
                }
            } else {
                return Optional.of(basicHttpResponse);
            }
        } else if(url.contains("search.jimmyjazz")){
            return Optional.of(basicHttpResponse);
        }

        return Optional.empty();
    }

    private Optional<String> getChallengeUrl(BasicHttpResponse basicHttpResponse, String url) throws Exception {
        if (!basicHttpResponse.getBody().isPresent() || !basicHttpResponse.getResponseCode().isPresent()) {
            return Optional.empty();
        }

        URL uri = new URL(url);
        String base = uri.getProtocol() + "://" + uri.getHost();

        String shortUrl = uri.getHost();
        Pattern passPattern = Pattern.compile("name=\"pass\" value=\"([^\"]*)\"");
        Pattern jschlPattern= Pattern.compile("name=\"jschl_vc\" value=\"([^\"]*)\"");
        Pattern sPattern = Pattern.compile("name=\"s\" value=\"([^\"]*)\"");
        Matcher passMatcher = passPattern.matcher(basicHttpResponse.getBody().get());
        Matcher jschlMatcher = jschlPattern.matcher(basicHttpResponse.getBody().get());
        Matcher sMatcher = sPattern.matcher(basicHttpResponse.getBody().get());

        String pass, jschl, s;
        if(passMatcher.find() && jschlMatcher.find() && sMatcher.find()){
            pass = passMatcher.group(1);
            jschl = jschlMatcher.group(1);
            s = sMatcher.group(1);
        } else {
            return Optional.empty();
        }

        String[] parts = basicHttpResponse.getBody().get().split("\n");
        String exec = "var t ='" + shortUrl + "';var a = {}";
        String firstLine = "", secondLine = "";
        boolean found = false;
        for(int i = 0; i < parts.length; i++){
            if(parts[i].contains("setTimeout(function()")){
                firstLine = parts[i + 1];
                secondLine = parts[i + 22];
                found = true;
                break;
            }
        }

        if(!found){
            return Optional.empty();
        }

        exec = firstLine + exec + secondLine.substring(0, secondLine.length() - 8);
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        String result = engine.eval(exec).toString();


        String template = "%s/cdn-cgi/l/chk_jschl?s=%s&jschl_vc=%s&pass=%s&jschl_answer=%s";
        String finalUrl = String.format(template, base, s, jschl, pass, result);
        return Optional.of(finalUrl);
    }

    private Optional<BasicHttpResponse> finalScan(BasicHttpResponse basicHttpResponse, BasicRequestClient basicRequestClient, String url){
        if(!basicHttpResponse.getBody().isPresent() || !basicHttpResponse.getResponseCode().isPresent()){
            return Optional.empty();
        } else if(basicHttpResponse.getResponseCode().get() == 403 && basicHttpResponse.getBody().get().contains("cdn-cgi")){
            BasicHttpResponse basicHttpResponse1 = bypassCap(basicRequestClient, url, basicRequestClient.getRequestConfig(), basicHttpResponse.getBody().get(), apiKeys[idx]);
            idx = (idx + 1) % apiKeys.length;
            return Optional.of(basicHttpResponse1);
        } else if(basicHttpResponse.getResponseCode().get() != 503){
            return Optional.of(basicHttpResponse);
        }

        return Optional.empty();
    }


    public BasicHttpResponse performGet(BasicRequestClient basicRequestClient,
                                        String url) {
        try {
            while(true) {
                Optional<BasicHttpResponse> basicHttpResponse = doGet(basicRequestClient, url);
                if(!basicHttpResponse.isPresent()){
                    log.error("cf get request failed on initial get");
                    return BasicHttpResponse.builder()
                            .body(Optional.empty())
                            .headers(Optional.empty())
                            .responseCode(Optional.empty())
                            .error(Optional.of(ResponseErrors.UNKNOWN))
                            .build();

                }

                if(isBanned(basicHttpResponse.get(), url, basicRequestClient.getHttpHost().getHostName())){
                    log.error("cf proxy banned");

                    return BasicHttpResponse.builder()
                            .body(Optional.empty())
                            .headers(Optional.empty())
                            .responseCode(Optional.empty())
                            .error(Optional.of(ResponseErrors.UNKNOWN))
                            .build();
                }

                Optional<BasicHttpResponse> basicHttpResponse1 = checkCapRoute(basicHttpResponse.get(), url, basicRequestClient);
                if(basicHttpResponse1.isPresent())
                    return basicHttpResponse1.get();
                Optional<String> resultUrl = getChallengeUrl(basicHttpResponse.get(), url);
                if(!resultUrl.isPresent()){
                    log.error("cf captcha failed");

                    return BasicHttpResponse.builder()
                            .body(Optional.empty())
                            .headers(Optional.empty())
                            .responseCode(Optional.empty())
                            .error(Optional.of(ResponseErrors.UNKNOWN))
                            .build();
                }

                Thread.sleep(4000 + (int) (Math.random() * 500));
                basicHttpResponse = doGet(basicRequestClient, resultUrl.get());

                if(!basicHttpResponse.isPresent()){
                    log.error("cf couldn't solve challenge URL");

                    return BasicHttpResponse.builder()
                            .body(Optional.empty())
                            .headers(Optional.empty())
                            .responseCode(Optional.empty())
                            .error(Optional.of(ResponseErrors.UNKNOWN))
                            .build();
                }

                basicHttpResponse = finalScan(basicHttpResponse.get(), basicRequestClient, url);

                if(basicHttpResponse.isPresent()){
                    return basicHttpResponse.get();
                }
            }
        } catch(Exception e) {
            log.error(EXCEPTION_LOG_MESSAGE, e);

            return BasicHttpResponse.builder()
                    .body(Optional.empty())
                    .headers(Optional.empty())
                    .responseCode(Optional.empty())
                    .error(Optional.of(ResponseErrors.UNKNOWN))
                    .build();
        }
    }


    private BasicHttpResponse bypassCap(BasicRequestClient basicRequestClient, String url, RequestConfig config, String respStr, String apiKey){
        String rayStr = "data-ray=\"([^\"]*)\"";
        String tokenStr = "data-sitekey=\"([^\"]*)\"";
        Pattern rayPattern = Pattern.compile(rayStr);
        Pattern tokenPattern = Pattern.compile(tokenStr);
        Matcher rayMatcher = rayPattern.matcher(respStr);
        Matcher tokenMatcher = tokenPattern.matcher(respStr);

        Pattern sPattern = Pattern.compile("name=\"s\" value=\"([^\"]*)\"");
        Matcher sMatcher = sPattern.matcher(respStr);


        if(rayMatcher.find() && tokenMatcher.find() && sMatcher.find()) {
            TwoCaptchaService t = new TwoCaptchaService(apiKey, tokenMatcher.group(1), url, basicRequestClient.getCloseableHttpClient());
            HttpGet httpGet = null;
            try {
                URL uri = new URL(url);
                String extraUrl = uri.getProtocol() + "://" + uri.getHost() + "/cdn-cgi/l/chk_captcha?s=" + URLEncoder.encode(sMatcher.group(1),  UTF_8) + "&id=" + URLEncoder.encode(rayMatcher.group(1), UTF_8) + "&g-recaptcha-response=";
                extraUrl += t.solveCaptcha();
                logger.info(extraUrl);
                httpGet = new HttpGet(extraUrl);
                httpGet.setConfig(config);
                HttpResponse httpResponse = basicRequestClient.getCloseableHttpClient().execute(httpGet);
                HttpEntity entity1 = httpResponse.getEntity();
                String responseString = EntityUtils.toString(entity1, "UTF-8");
                int respCode = httpResponse.getStatusLine().getStatusCode();

                if(responseString.contains("<title>Access denied | www.sneakersnstuff.com used Cloudflare to restrict access</title>")) {
                    log.info("banned proxy on sns " + basicRequestClient.getHttpHost().getHostName());

                    return BasicHttpResponse.builder()
                            .error(Optional.of(ResponseErrors.BANNED))
                            .responseCode(Optional.of(respCode))
                            .headers(Optional.empty())
                            .body(Optional.empty())
                            .build();
                } else if(responseString.contains("Access denied")){
                    log.info("banned proxy on sns " + basicRequestClient.getHttpHost().getHostName());

                    return BasicHttpResponse.builder()
                            .error(Optional.of(ResponseErrors.BANNED))
                            .responseCode(Optional.of(respCode))
                            .headers(Optional.empty())
                            .body(Optional.empty())
                            .build();
                }

                return BasicHttpResponse.builder()
                        .body(Optional.of(responseString))
                        .responseCode(Optional.of(respCode))
                        .error(Optional.empty())
                        .headers(Optional.empty())
                        .build();

            } catch(Exception e) {
                log.error(EXCEPTION_LOG_MESSAGE, e);

            } finally {
                if(httpGet != null)
                    httpGet.releaseConnection();
            }
        }

        log.error(respStr);

        return BasicHttpResponse.builder()
                .body(Optional.empty())
                .headers(Optional.empty())
                .responseCode(Optional.empty())
                .error(Optional.of(ResponseErrors.UNKNOWN))
                .build();
    }

    public BasicHttpResponse performPost(
            BasicRequestClient basicRequestClient,
            String url, String body){
        HttpPost httpPost = new HttpPost(url);

        if(basicRequestClient.getRequestConfig() != null && !url.contains("http://localhost")){
            httpPost.setConfig(basicRequestClient.getRequestConfig());
        }

        if(basicRequestClient.getHeaderList() != null){
            for(Header header : basicRequestClient.getHeaderList()) {
                httpPost.setHeader(header);
            }
        }

        org.apache.http.HttpResponse httpResponse;
        try {
            httpPost.setEntity(new StringEntity(body));
            String responseString;
            int statusCode;
            while(true) {

                try {
                    httpResponse = basicRequestClient.getCloseableHttpClient().execute(httpPost);

                    if (httpResponse.getStatusLine().getStatusCode() == 503 || httpResponse.getStatusLine().getStatusCode() == 403) {
                        performGet(basicRequestClient, UrlHelper.deriveBaseUrl(url));
                        Timeout.timeout(1000);
                    } else {
                        HttpEntity entity = httpResponse.getEntity();
                        statusCode = httpResponse.getStatusLine().getStatusCode();
                        responseString = EntityUtils.toString(entity);
                        break;
                    }

                } finally {
                    httpPost.releaseConnection();
                }

            }


            return BasicHttpResponse.builder()
                    .responseCode(Optional.of(statusCode))
                    .body(Optional.of(responseString))
                    .headers(Optional.empty())
                    .error(Optional.empty())
                    .build();
        } catch(Exception e) {
            log.error(EXCEPTION_LOG_MESSAGE, e);
            return BasicHttpResponse.builder()
                    .responseCode(Optional.empty())
                    .body(Optional.empty())
                    .headers(Optional.empty())
                    .error(Optional.of(ResponseErrors.UNKNOWN))
                    .build();
        }
    }
}
