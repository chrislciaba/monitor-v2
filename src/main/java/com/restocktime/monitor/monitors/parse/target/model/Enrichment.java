package com.restocktime.monitor.monitors.parse.target.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Enrichment {
    private List<Images> images;

    public List<Images> getImages() {
        return images;
    }
}
