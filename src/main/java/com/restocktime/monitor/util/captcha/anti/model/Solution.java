package com.restocktime.monitor.util.captcha.anti.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

@Data
public class Solution {
    @JsonProperty("gRecaptchaResponse")
    private String gRecaptchaResponse;
}
