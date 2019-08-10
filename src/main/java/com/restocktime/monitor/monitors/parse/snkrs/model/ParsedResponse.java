package com.restocktime.monitor.monitors.parse.snkrs.model;

public class ParsedResponse {
    private String id;
    private String name;
    private String slug;
    private String type;
    private String image;
    private String selectionEngine;
    private String launchDate;
    private Long launchDateEpoch;
    private String sku;
    private boolean available;

    public ParsedResponse(String id, String name, String slug, String type, String image, String selectionEngine, String launchDate, Long launchDateEpoch, String sku, boolean available) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.type = type;
        this.image = image;
        this.selectionEngine = selectionEngine;
        this.launchDate = launchDate;
        this.launchDateEpoch = launchDateEpoch;
        this.sku = sku;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public String getSelectionEngine() {
        return selectionEngine;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public Long getLaunchDateEpoch(){
        return launchDateEpoch;
    }

    public String getSku() {
        return sku;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
