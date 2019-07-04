package com.restocktime.monitor.monitors.parse.frenzy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class FrenzyResponse {
    private List<FrenzyProduct> flashsales;


    public List<FrenzyProduct> getFlashsales() {
        return flashsales;
    }

    public void setFlashsales(List<FrenzyProduct> flashsales) {
        this.flashsales = flashsales;
    }
}
