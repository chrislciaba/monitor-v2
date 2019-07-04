package com.restocktime.monitor.monitors.parse.footsites.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootsitesProduct {
    private List<VariantAttribute> variantAttributes;

    public List<VariantAttribute> getVariantAttributes() {
        return variantAttributes;
    }

    public void setVariantAttributes(List<VariantAttribute> variantAttributes) {
        this.variantAttributes = variantAttributes;
    }
}
