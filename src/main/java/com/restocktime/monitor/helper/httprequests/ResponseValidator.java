package com.restocktime.monitor.helper.httprequests;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;

public class ResponseValidator {

    public static boolean isInvalid(BasicHttpResponse basicHttpResponse){
        return !(basicHttpResponse.getResponseCode().isPresent() && basicHttpResponse.getBody().isPresent() && !basicHttpResponse.getError().isPresent());
    }
}
