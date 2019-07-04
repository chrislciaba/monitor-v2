package com.restocktime.monitor.monitors.parse.offwhite.model.offwhite;

public class OffWhiteSize {
    private Integer id;
    private String name;
    private Boolean preorder_only;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPreorder_only() {
        return preorder_only;
    }

    public void setPreorder_only(Boolean preorder_only) {
        this.preorder_only = preorder_only;
    }
}
