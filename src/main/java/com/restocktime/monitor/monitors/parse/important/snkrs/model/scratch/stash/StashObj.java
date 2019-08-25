package com.restocktime.monitor.monitors.parse.important.snkrs.model.scratch.stash;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StashObj {
    private Locations locations;
    private String invite;
    private String inviteImageUrl;
    private String theme;

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

    public String getInviteImageUrl() {
        return inviteImageUrl;
    }

    public void setInviteImageUrl(String inviteImageUrl) {
        this.inviteImageUrl = inviteImageUrl;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
