package com.restocktime.monitor.monitors.parse.social.instagram.model.stories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reel {
    private List<Story> items;

    public List<Story> getItems() {
        return items;
    }

    public void setItems(List<Story> items) {
        this.items = items;
    }
}
