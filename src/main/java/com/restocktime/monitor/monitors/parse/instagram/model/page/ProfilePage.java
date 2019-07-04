package com.restocktime.monitor.monitors.parse.instagram.model.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilePage {
    private GraphQl graphql;

    public GraphQl getGraphql() {
        return graphql;
    }

    public void setGraphql(GraphQl graphql) {
        this.graphql = graphql;
    }
}
