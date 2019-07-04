package com.restocktime.monitor.monitors.parse.footpatrol.model.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FpProduct {
    @JsonProperty("ID")
    private String ID;
    private String name;
    private String mainImage;
    private Map<String, FpOption> options;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Map<String, FpOption> getOptions() {
        return options;
    }

    public void setOptions(Map<String, FpOption> options) {
        this.options = options;
    }
}
