package com.restocktime.monitor.monitors.parse.important.snkrs.model.snkrs.snkrsv1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restocktime.monitor.monitors.parse.important.snkrs.model.snkrs.ProductBase;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product extends ProductBase {
    private String style;
    private String colorCode;
    private String selectionEngine;
    private String startSellDate;

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
}
