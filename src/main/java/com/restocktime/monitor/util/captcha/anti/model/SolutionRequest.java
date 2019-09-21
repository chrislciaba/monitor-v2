package com.restocktime.monitor.util.captcha.anti.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class SolutionRequest {
     private String clientKey;
     private Integer taskId;
}
