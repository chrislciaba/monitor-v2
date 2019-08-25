package com.restocktime.monitor.monitors.parse.social.instagram.model.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class EdgeMediaToCaption {
    private List<InnerEdge> edges;

    public List<InnerEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<InnerEdge> edges) {
        this.edges = edges;
    }
}
