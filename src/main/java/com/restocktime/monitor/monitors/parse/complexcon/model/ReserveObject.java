package com.restocktime.monitor.monitors.parse.complexcon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)


public class ReserveObject {
    private List<MarketPlace> data;

    public List<MarketPlace> getData() {
        return data;
    }

    public void setData(List<MarketPlace> data) {
        this.data = data;
    }
}
