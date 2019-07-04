package com.restocktime.monitor.monitors.parse.instagram.model.stories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {
    private String id;
    private String display_url;
    private String story_cta_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    public String getStory_cta_url() {
        return story_cta_url;
    }

    public void setStory_cta_url(String story_cta_url) {
        this.story_cta_url = story_cta_url;
    }
}
