package com.restocktime.monitor.monitors.parse.important.shopify.helper;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutLinkRedirectHelper {

    private Pattern linkPattern;

    public CheckoutLinkRedirectHelper(){
        linkPattern = Pattern.compile("url=([^\"]*)\"");
    }
    public BasicHttpResponse getCheckoutPage(String url, BasicRequestClient basicRequestClient, HttpRequestHelper httpRequestHelper){
        BasicHttpResponse checkoutHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
        while(checkoutHttpResponse.getBody() != null && checkoutHttpResponse.getBody().get().contains("http-equiv=\"refresh\"")){
            Matcher m = linkPattern.matcher(checkoutHttpResponse.getBody().get());

            if(m.find()) {
                checkoutHttpResponse = httpRequestHelper.performGet(basicRequestClient, m.group(1));
            }
        }

        return checkoutHttpResponse;
    }
}
