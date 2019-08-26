package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class SizeAndStock {
    private String size;
    private String stock;
}
