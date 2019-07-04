package com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.exp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String selectionEngine;
    private String startSellDate;
    private String style;
    private String colorCode;

    public String getSelectionEngine() {
        return selectionEngine;
    }

    public void setSelectionEngine(String selectionEngine) {
        this.selectionEngine = selectionEngine;
    }

    public String getStartSellDate() {
        return startSellDate;
    }

    public void setStartSellDate(String startSellDate) {
        this.startSellDate = startSellDate;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
