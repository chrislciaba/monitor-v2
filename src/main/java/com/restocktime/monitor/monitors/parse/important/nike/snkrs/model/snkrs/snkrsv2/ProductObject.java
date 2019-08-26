package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.ProductBase;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductObject extends ProductBase {
    private String id;
    private PublishedContent publishedContent;
    private List<ProductInfo> productInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PublishedContent getPublishedContent() {
        return publishedContent;
    }

    public void setPublishedContent(PublishedContent publishedContent) {
        this.publishedContent = publishedContent;
    }

    public List<ProductInfo> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(List<ProductInfo> productInfo) {
        this.productInfo = productInfo;
    }


}
