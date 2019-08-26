package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.scratch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hunts {
    private List<Hunt> objects;

    public List<Hunt> getObjects() {
        return objects;
    }

    public void setObjects(List<Hunt> objects) {
        this.objects = objects;
    }
}
