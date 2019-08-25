package com.restocktime.monitor.monitors.parse.important.footdistrict.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FootdistrictSearch {
    private List<FootdistricResult> results;

    public List<FootdistricResult> getResults() {
        return results;
    }

    public void setResults(List<FootdistricResult> results) {
        this.results = results;
    }
}
