package com.restocktime.monitor.monitors.parse.important.snkrs.model.snkrs.snkrsv2;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishedContent {
    private List<Node> nodes;
    private ContentProperties properties;
    private String viewStartDate;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public ContentProperties getProperties() {
        return properties;
    }

    public void setProperties(ContentProperties properties) {
        this.properties = properties;
    }

    public String getViewStartDate() {
        return viewStartDate;
    }

    public void setViewStartDate(String viewStartDate) {
        this.viewStartDate = viewStartDate;
    }
}
