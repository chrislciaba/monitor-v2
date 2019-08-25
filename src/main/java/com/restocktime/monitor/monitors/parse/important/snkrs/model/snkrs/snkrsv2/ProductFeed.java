package com.restocktime.monitor.monitors.parse.important.snkrs.model.snkrs.snkrsv2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductFeed {
    List<ProductObject> objects;

    public List<ProductObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ProductObject> objects) {
        this.objects = objects;
    }

}
