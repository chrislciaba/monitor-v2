package com.restocktime.monitor.monitors.parse.target.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Images {
    private String primary;

    public String getPrimary() {
        return primary;
    }
}
