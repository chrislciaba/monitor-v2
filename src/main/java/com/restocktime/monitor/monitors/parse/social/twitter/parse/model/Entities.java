package com.restocktime.monitor.monitors.parse.social.twitter.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entities {
    private List<UserMention> user_mentions;
    private List<Media> media;
}
