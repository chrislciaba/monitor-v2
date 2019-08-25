package com.restocktime.monitor.monitors.parse.social.instagram.model.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class InnerEdge {

    private InnerNode node;

    public InnerNode getNode() {
        return node;
    }

    public void setNode(InnerNode node) {
        this.node = node;
    }
}
