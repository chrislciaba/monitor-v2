package com.restocktime.monitor.monitors.parse.funko.walmart.model.terra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restocktime.monitor.monitors.parse.important.snkrs.model.scratch.Images;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {
    Map<String, Product> products;
    Map<String, Offer> offers;
    Map<String, Images> images;

    public Map<String, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Product> products) {
        this.products = products;
    }

    public Map<String, Offer> getOffers() {
        return offers;
    }

    public void setOffers(Map<String, Offer> offers) {
        this.offers = offers;
    }

    public Map<String, Images> getImages() {
        return images;
    }

    public void setImages(Map<String, Images> images) {
        this.images = images;
    }
}
