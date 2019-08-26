package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductInfo {
    private LaunchView launchView;
    private MerchProduct merchProduct;
    private Availability availability;
    private List<AvailableSku> availableSkus;
    private List<Sku> skus;
}
