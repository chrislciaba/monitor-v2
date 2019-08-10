package com.restocktime.monitor.util.httprequests;

import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;

public class ResponseValidator {

    public static boolean isInvalid(BasicHttpResponse basicHttpResponse){
        return !(basicHttpResponse.getResponseCode().isPresent() && basicHttpResponse.getBody().isPresent() && !basicHttpResponse.getError().isPresent());
    }
}
