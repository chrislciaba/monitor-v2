package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSubInfo {
    private LaunchView launchView;

    public LaunchView getLaunchView() {
        return launchView;
    }

    public void setLaunchView(LaunchView launchView) {
        this.launchView = launchView;
    }
}
