package com.restocktime.monitor.util.http.request;

import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;

public class ResponseValidator {

    public static boolean isInvalid(BasicHttpResponse basicHttpResponse){
        return !(basicHttpResponse.getResponseCode().isPresent() && basicHttpResponse.getBody().isPresent() && !basicHttpResponse.getError().isPresent());
    }
}
