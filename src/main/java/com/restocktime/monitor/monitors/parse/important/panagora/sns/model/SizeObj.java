package com.restocktime.monitor.monitors.parse.important.panagora.sns.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SizeObj {
    private String sku;
    private String size;
}
