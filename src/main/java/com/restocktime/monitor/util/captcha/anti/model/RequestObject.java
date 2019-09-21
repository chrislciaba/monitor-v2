package com.restocktime.monitor.util.captcha.anti.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class RequestObject {
    private String clientKey;
    private RequestTask task;
    private Integer softId;
    private String languagePool;
}
