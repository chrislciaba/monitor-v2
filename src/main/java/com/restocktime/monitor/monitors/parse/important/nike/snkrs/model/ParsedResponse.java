package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ParsedResponse {
    private String id;
    private String name;
    private String slug;
    private String type;
    private String image;
    private String selectionEngine;
    private String launchDate;
    private Long launchDateEpoch;
    private String sku;
    private boolean available;
    private List<SizeAndStock> sizeAndStocks;
}
