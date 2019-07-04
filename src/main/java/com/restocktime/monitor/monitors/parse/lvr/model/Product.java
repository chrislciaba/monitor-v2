package com.restocktime.monitor.monitors.parse.lvr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @JsonProperty("ItemId")
    private String itemId;

    @JsonProperty
    private String description;

    @JsonProperty("DesignerDescription")
    private String designerDescription;

    @JsonProperty("Available")
    private Boolean available;

    @JsonProperty("ImagePath")
    private String imagePath;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignerDescription() {
        return designerDescription;
    }

    public void setDesignerDescription(String designerDescription) {
        this.designerDescription = designerDescription;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
