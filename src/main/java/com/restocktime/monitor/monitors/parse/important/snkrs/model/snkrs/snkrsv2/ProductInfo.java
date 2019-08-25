package com.restocktime.monitor.monitors.parse.important.snkrs.model.snkrs.snkrsv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductInfo {
    private LaunchView launchView;
    private MerchProduct merchProduct;
    private Availability availability;
    private List<AvailableSku> availableSkus;

    public List<AvailableSku> getAvailableSkus() {
        return availableSkus;
    }

    public void setAvailableSkus(List<AvailableSku> availableSkus) {
        this.availableSkus = availableSkus;
    }

    public LaunchView getLaunchView() {
        return launchView;
    }

    public void setLaunchView(LaunchView launchView) {
        this.launchView = launchView;
    }

    public MerchProduct getMerchProduct() {
        return merchProduct;
    }

    public void setMerchProduct(MerchProduct merchProduct) {
        this.merchProduct = merchProduct;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
}