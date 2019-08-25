package com.restocktime.monitor.monitors.parse.funko.target.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetProduct {
    private OnlineInfo onlineInfo;
    private Images images;
    private AvailabilityToPromiseNetwork available_to_promise_network;
    private Item item;
    private Price price;


    public AvailabilityToPromiseNetwork getAvailable_to_promise_network() {
        return available_to_promise_network;
    }

    public Item getItem() {
        return item;
    }

    public Price getPrice() {
        return price;
    }


    public OnlineInfo getOnlineInfo() {
        return onlineInfo;
    }

    public void setOnlineInfo(OnlineInfo onlineInfo) {
        this.onlineInfo = onlineInfo;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}
