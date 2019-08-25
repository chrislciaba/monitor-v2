package com.restocktime.monitor.monitors.parse.social.instagram.model.stories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private List<Reel> reels_media;

    public List<Reel> getReels_media() {
        return reels_media;
    }

    public void setReels_media(List<Reel> reels_media) {
        this.reels_media = reels_media;
    }
}
