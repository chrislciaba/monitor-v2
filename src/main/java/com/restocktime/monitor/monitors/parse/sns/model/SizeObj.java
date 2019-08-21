package com.restocktime.monitor.monitors.parse.sns.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SizeObj {
    private String sku;
    private String size;
}
