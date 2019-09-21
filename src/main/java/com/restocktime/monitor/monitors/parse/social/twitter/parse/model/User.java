package com.restocktime.monitor.monitors.parse.social.twitter.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
    private String screen_name;
    private String location;
    private String description;
    private String profile_image_url_https;
    private String profile_banner_url;
}
