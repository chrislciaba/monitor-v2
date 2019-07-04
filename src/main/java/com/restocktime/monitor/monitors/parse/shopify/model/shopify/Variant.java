package com.restocktime.monitor.monitors.parse.shopify.model.shopify;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Variant {
    private String id;
    private Boolean available;
    private String title;
    private Integer inventory_quantity;
    private String price;
    private String option1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getInventory_quantity() {
        return inventory_quantity;
    }

    public void setInventory_quantity(Integer inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }
}
