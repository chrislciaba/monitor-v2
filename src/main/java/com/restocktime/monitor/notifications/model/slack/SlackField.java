package com.restocktime.monitor.notifications.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackField {
    private String title;
    private String title_link;
    private String value;
    @JsonProperty("short")
    private Boolean _short;


    public SlackField(String title, String value) {

        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.value = value.replaceAll("[^\\x00-\\x7F]", "");
    }

    public SlackField(String title, String value, Boolean _short) {

        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.value = value.replaceAll("[^\\x00-\\x7F]", "");
        this._short = _short;
    }

    public SlackField(SlackField slackField) {
        this.title = slackField.title;
        this.value = slackField.value;
        this._short = slackField._short;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_link() {
        return title_link;
    }

    public void setTitle_link(String title_link) {
        this.title_link = title_link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean get_short() {
        return _short;
    }

    public void set_short(Boolean _short) {
        this._short = _short;
    }
}
