package com.restocktime.monitor.monitors.parse.social.twitter.parse.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMention {
    private String screen_name;
    private String id_str;
}
