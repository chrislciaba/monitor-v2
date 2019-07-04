package com.restocktime.monitor.monitors.parse.offwhite.model.offwhite;

import java.util.List;

public class OffWhiteJson {
    public List<OffWhiteSize> available_sizes;

    public List<OffWhiteSize> getAvailable_sizes() {
        return available_sizes;
    }

    public void setAvailable_sizes(List<OffWhiteSize> available_sizes) {
        this.available_sizes = available_sizes;
    }
}
