package com.restocktime.monitor.monitors.parse.important.supreme.model.supreme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Size {
    private String name;
    private Integer id;
    private Integer stock_level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStock_level() {
        return stock_level;
    }

    public void setStock_level(Integer stock_level) {
        this.stock_level = stock_level;
    }
}
