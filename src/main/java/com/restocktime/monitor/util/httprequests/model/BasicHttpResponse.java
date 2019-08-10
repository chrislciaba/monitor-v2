package com.restocktime.monitor.util.httprequests.model;

import lombok.Builder;
import lombok.Getter;
import org.apache.http.Header;

import java.util.List;
import java.util.Optional;

@Builder
@Getter
public class BasicHttpResponse {
    private Optional<String> body;
    private Optional<Integer> responseCode;
    private Optional<List<Header>> headers;
    private Optional<ResponseErrors> error;

}
