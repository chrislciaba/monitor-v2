package com.restocktime.monitor.monitors.parse.oneblockdown.model.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObdProduct {
    private String permalink;
    private Boolean isAvailable;
    private Boolean isInPreOrder;
    private Boolean isOutOfStock;
    private Manufacturer manufacturer;
    private String title;

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    public Boolean getIsInPreOrder() {
        return isInPreOrder;
    }

    public void setIsInPreOrder(Boolean inPreOrder) {
        isInPreOrder = inPreOrder;
    }

    public Boolean getIsOutOfStock() {
        return isOutOfStock;
    }

    public void setIsOutOfStock(Boolean outOfStock) {
        isOutOfStock = outOfStock;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
