package com.restocktime.monitor.monitors.parse.adidas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Availability {
    private String availability_status;
    private List<Variation> variation_list;

    public String getAvailability_status() {
        return availability_status;
    }

    public void setAvailability_status(String availability_status) {
        this.availability_status = availability_status;
    }

    public List<Variation> getVariation_list() {
        return variation_list;
    }

    public void setVariation_list(List<Variation> variation_list) {
        this.variation_list = variation_list;
    }
}
