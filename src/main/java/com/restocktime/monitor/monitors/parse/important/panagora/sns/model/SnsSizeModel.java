package com.restocktime.monitor.monitors.parse.important.panagora.sns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsSizeModel {

    @JsonProperty("converted-size-size-us")
    private String us;
}
