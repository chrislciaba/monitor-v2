package com.restocktime.monitor.util.captcha.anti.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Value;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObject {
    private Integer errorId;
    private Integer taskId;
    private String taskStatus;
    private Solution solution;
}
