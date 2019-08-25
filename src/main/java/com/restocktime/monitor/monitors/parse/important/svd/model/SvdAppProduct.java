package com.restocktime.monitor.monitors.parse.important.svd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvdAppProduct {
    @JsonProperty("view_type")
    private String viewType;

    @JsonProperty("general_data")
    private SvdGeneralData generalData;
}
