package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentProperties {
    private SEO seo;
    private Custom custom;
    private String title;
    private String subtitle;
}
