package com.restocktime.monitor.monitors.parse.funko.target.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {
    private ListPrice listPrice;

    public ListPrice getListPrice() {
        return listPrice;
    }
}
