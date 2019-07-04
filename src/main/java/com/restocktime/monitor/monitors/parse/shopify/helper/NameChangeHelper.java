package com.restocktime.monitor.monitors.parse.shopify.helper;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameChangeHelper {

    private final String NAME_PATTERN_STR = "<span class=\"product__description__name order-summary__emphasis\">([^<]*)</span>";
    private Pattern productNamePattern;

    public NameChangeHelper(){
        productNamePattern = Pattern.compile(NAME_PATTERN_STR);
    }

    public boolean parse(BasicHttpResponse basicHttpResponse, Map<String, String> urlToNameMap, String url) {
        if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {
            return false;
        }

        String responseString = basicHttpResponse.getBody();

        try {
            Matcher productNameMatcher = productNamePattern.matcher(responseString);
            if(productNameMatcher.find()){
                String newName = productNameMatcher.group(1);
                              if(urlToNameMap.containsKey(url) && urlToNameMap.get(url).equals(productNameMatcher.group(1))){

                    return false;
                } else {

                    urlToNameMap.put(url, productNameMatcher.group(1));
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }
}
