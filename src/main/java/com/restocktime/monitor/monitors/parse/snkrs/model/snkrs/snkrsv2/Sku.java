package com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sku {
    private Boolean available;

}
