package com.restocktime.monitor.monitors.parse.shopify.helper;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.url.UrlHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetApiToken {
    private static final Pattern tokenPattern = Pattern.compile("<meta\\s+name=\"shopify-checkout-api-token\"\\s+content=\"([^\"]*)\">");
    private static final Pattern backupTokenPattern = Pattern.compile("<script\\s+id=\"shopify-features\"\\s+type=\"application/json\">\\{\"accessToken\":\"([^\"]*)\"");

    public static String getApiToken(HttpRequestHelper httpRequestHelper, BasicRequestClient basicRequestClient, String url){
        if(url.contains("https://cactusplantfleamarket.myshopify.com")){
            return "574d05e81d915e3c13a16c514c678649";
        }
        BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, UrlHelper.deriveBaseUrl(url));
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return null;
        }

        Matcher tokenMatcher = tokenPattern.matcher(basicHttpResponse.getBody().get());
        Matcher backupTokenMatcher = backupTokenPattern.matcher(basicHttpResponse.getBody().get());
        if(tokenMatcher.find()){
            return tokenMatcher.group(1);
        } else if(backupTokenMatcher.find()){
            return backupTokenMatcher.group(1);
        }

        return null;
    }


}
