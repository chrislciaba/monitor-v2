package com.restocktime.monitor.monitors.parse.target.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDescription {
    private String title;

    public String getTitle() {
        return title;
    }
}
