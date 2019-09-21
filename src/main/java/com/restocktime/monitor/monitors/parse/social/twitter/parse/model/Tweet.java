package com.restocktime.monitor.monitors.parse.social.twitter.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {
    private String full_text;
    private Entities entities;
    private String user_id_str;
}
