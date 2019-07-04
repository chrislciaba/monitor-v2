package com.restocktime.monitor.monitors.parse.instagram.model.page;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class EdgeOwnerToTimelineMedia {
    private List<Edge> edges;

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}
