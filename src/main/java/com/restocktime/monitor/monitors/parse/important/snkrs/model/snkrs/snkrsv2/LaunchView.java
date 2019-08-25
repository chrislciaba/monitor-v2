package com.restocktime.monitor.monitors.parse.important.snkrs.model.snkrs.snkrsv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LaunchView {
    private String method;
    private String startEntryDate;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStartEntryDate() {
        return startEntryDate;
    }

    public void setStartEntryDate(String startEntryDate) {
        this.startEntryDate = startEntryDate;
    }
}
