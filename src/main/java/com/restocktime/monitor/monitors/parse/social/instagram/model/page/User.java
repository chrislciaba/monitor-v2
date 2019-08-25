package com.restocktime.monitor.monitors.parse.social.instagram.model.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String biography;
    private String full_name;
    private EdgeOwnerToTimelineMedia edge_owner_to_timeline_media;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public EdgeOwnerToTimelineMedia getEdge_owner_to_timeline_media() {
        return edge_owner_to_timeline_media;
    }

    public void setEdge_owner_to_timeline_media(EdgeOwnerToTimelineMedia edge_owner_to_timeline_media) {
        this.edge_owner_to_timeline_media = edge_owner_to_timeline_media;
    }
}
