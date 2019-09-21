package com.restocktime.monitor.monitors.parse.social.twitter.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalObject {
    private Map<String, Tweet> tweets;
    private Map<String, User> users;
}
