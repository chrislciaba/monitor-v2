package com.restocktime.monitor.monitors.parse.funko.target.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OnlineInfo {
    String availabilityCode;
    Price price;

    public String getAvailabilityCode() {
        return availabilityCode;
    }

    public Price getPrice() {return price;}

    public void setAvailabilityCode(String availabilityCode) {
        this.availabilityCode = availabilityCode;
    }
}
