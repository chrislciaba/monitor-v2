package com.restocktime.monitor.monitors.parse.target.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListPrice {
    public String formattedPrice;

    public String getFormattedPrice() {
        return formattedPrice;
    }
}
