package com.restocktime.monitor.monitors.parse.oneblockdown.model.product;

import java.util.List;

public class Product {
    List<Variant> variants;

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }
}
