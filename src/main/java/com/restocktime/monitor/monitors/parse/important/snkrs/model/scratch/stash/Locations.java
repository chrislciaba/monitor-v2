package com.restocktime.monitor.monitors.parse.important.snkrs.model.scratch.stash;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Locations {
    private List<Stash> stashes;

    public List<Stash> getStashes() {
        return stashes;
    }

    public void setStashes(List<Stash> stashes) {
        this.stashes = stashes;
    }
}
