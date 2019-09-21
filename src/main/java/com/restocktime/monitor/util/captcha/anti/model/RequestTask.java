package com.restocktime.monitor.util.captcha.anti.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class RequestTask {
    private String type;
    private String websiteKey;
    private String websiteURL;
}
