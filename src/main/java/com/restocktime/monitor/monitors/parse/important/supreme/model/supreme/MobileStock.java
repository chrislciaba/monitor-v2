package com.restocktime.monitor.monitors.parse.important.supreme.model.supreme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileStock {
    @JsonProperty("products_and_categories")
    private  Map<String, List<AllProduct>> categories;

    public Map<String, List<AllProduct>> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, List<AllProduct>> categories) {
        this.categories = categories;
    }
}
