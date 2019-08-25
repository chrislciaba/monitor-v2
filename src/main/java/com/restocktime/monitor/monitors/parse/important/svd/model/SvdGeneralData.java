package com.restocktime.monitor.monitors.parse.important.svd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvdGeneralData {
    private String sku;
    private String name;
    private String url;
    private String brand;
    private List<String> images;
    private Boolean soldout;
}
