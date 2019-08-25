package com.restocktime.monitor.monitors.parse.important.shopify.model.products;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restocktime.monitor.monitors.parse.important.shopify.model.api.Image;
import com.restocktime.monitor.monitors.parse.important.shopify.model.shopify.Variant;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private Long id;
    private String title;
    private String handle;
    private List<String> tags;
    private List<Variant> variants;
    private List<Image> images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
