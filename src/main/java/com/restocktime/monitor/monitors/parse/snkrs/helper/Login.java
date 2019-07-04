package com.restocktime.monitor.monitors.parse.snkrs.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.Akamai;
import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv2.LoginResponse;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Login {
    final static Logger logger = Logger.getLogger(Login.class);

    public static String getToken(BasicRequestClient basicRequestClient, HttpRequestHelper httpRequestHelper){
        final String template = "{\"sensor_data\":\"%s\"}";
        BasicCookieStore cookieStore = new BasicCookieStore();


        try {
            String s;

            RequestConfig config1 = RequestConfig.custom()
                    .setConnectTimeout(10 * 1000)
                    .setConnectionRequestTimeout(10 * 1000)
                    .setSocketTimeout(10 * 1000)
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .build();


            Akamai akamai = new Akamai();
            akamai.setUrl("https://s3.nikecdn.com/unite/mobile.html");
            List<Header> headerList = new ArrayList<>();
            headerList.add(new BasicHeader("Connection", "Keep-Alive"));
            headerList.add(new BasicHeader("X-NewRelic-ID", "VQYGVF5SCBAJVlFaAQIH"));
            headerList.add(new BasicHeader("Origin", "https://s3.nikecdn.com/unite/mobile.html"));
            headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36"));
            headerList.add(new BasicHeader("Connection", "Keep-Alive"));
            headerList.add(new BasicHeader("Content-Type", "text/plain;charset=UTF-8"));
            headerList.add(new BasicHeader("Accept", "**"));
            headerList.add(new BasicHeader("Referer", "https://s3.nikecdn.com"));
            headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate, br"));
            headerList.add(new BasicHeader("Accept-Language", "en-US,en;q=0.9,ms;q=0.8"));

            basicRequestClient.setHeaderList(headerList);


            String body = String.format(template, akamai.generateSensorData());
            logger.info(body);

            com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse httpResponse1 = httpRequestHelper.performPost(basicRequestClient,"https://s3.nikecdn.com/_bm/_data", body);
            for(org.apache.http.cookie.Cookie cookie : cookieStore.getCookies()){
                logger.info(cookie.getName());
                if(cookie.getName().equals("_abck")){
                    akamai.setCookie(cookie.getValue());
                    break;
                }
            }


            body = String.format(template, akamai.generateSensorData1());
            com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse httpResponse2 = httpRequestHelper.performPost(basicRequestClient, "https://s3.nikecdn.com/_bm/_data", body);


            if(httpResponse2.getBody().contains("false")){
                exit(1);
            }

            String login = "{\"username\":\"chrislciaba@gmail.com\",\"password\":\"Kingsmma1*\",\"client_id\":\"G64vA0b95ZruUtGk1K0FkAgaO3Ch30sj\",\"ux_id\":\"com.nike.commerce.snkrs.ios\",\"grant_type\":\"password\"}";
            logger.info(login);
            List<Header> loginHeaders = new ArrayList<>();
            loginHeaders.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216"));
            loginHeaders.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));
            loginHeaders.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
            loginHeaders.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.9,ms;q=0.8"));
            loginHeaders.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
            loginHeaders.add(new BasicHeader("Connection", "Keep-Alive"));
            basicRequestClient.setHeaderList(loginHeaders);

            //logger.info(pyResp.getData());
            BasicHttpResponse basicHttpResponse3 = httpRequestHelper.performPost(basicRequestClient, "https://s3.nikecdn.com/login?appVersion=515&experienceVersion=413&uxid=com.nike.commerce.snkrs.ios&locale=en_US&backendEnvironment=identity&browser=Apple%20Computer%2C%20Inc.&os=undefined&mobile=true&native=true&visit=1&visitor=2614e655-525e-43d8-b0d8-980d87a31346", login);

            if(basicHttpResponse3 == null || basicHttpResponse3.getBody() == null){
                return null;
            }
            logger.info(basicHttpResponse3.getBody());
            String responseString = basicHttpResponse3.getBody();
            logger.info(responseString);
            logger.info(basicHttpResponse3.getResponseCode());
            ObjectMapper objectMapper = new ObjectMapper();
            LoginResponse loginResponse = objectMapper.readValue(responseString, LoginResponse.class);
            return loginResponse.getAccess_token();
        } catch(Exception e){

        }
        return null;
    }
}

