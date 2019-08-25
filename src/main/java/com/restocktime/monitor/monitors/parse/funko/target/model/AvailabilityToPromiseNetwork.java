package com.restocktime.monitor.monitors.parse.funko.target.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailabilityToPromiseNetwork {
    private int online_available_to_promise_quantity;
    private String availability_status;

    public int getOnline_available_to_promise_quantity() {
        return online_available_to_promise_quantity;
    }

    public String getAvailability_status() {
        return availability_status;
    }
}
