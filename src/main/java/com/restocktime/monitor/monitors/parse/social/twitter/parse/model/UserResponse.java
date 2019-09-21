package com.restocktime.monitor.monitors.parse.social.twitter.parse.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private GlobalObject globalObjects;
}
