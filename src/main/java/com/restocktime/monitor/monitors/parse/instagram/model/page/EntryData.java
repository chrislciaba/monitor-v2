package com.restocktime.monitor.monitors.parse.instagram.model.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class EntryData {
    @JsonProperty("ProfilePage")
    private List<ProfilePage> profilePage;

    public List<ProfilePage> getProfilePage() {
        return profilePage;
    }

    public void setProfilePage(List<ProfilePage> profilePage) {
        this.profilePage = profilePage;
    }
}
