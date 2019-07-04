package com.restocktime.monitor.notifications.model.slack.button;

import java.util.List;

public class ButtonAction {
    private String name;
    private String text;
    private String type;
    private List<ButtonOption> options;
    private String value;
    private String url;

    public ButtonAction(String name, String text, String type, List<ButtonOption> options, String value, String url) {
        this.name = name;
        this.text = text;
        this.type = type;
        this.options = options;
        this.value = value;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ButtonOption> getOptions() {
        return options;
    }

    public void setOptions(List<ButtonOption> options) {
        this.options = options;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
