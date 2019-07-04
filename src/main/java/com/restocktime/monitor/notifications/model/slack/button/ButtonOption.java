package com.restocktime.monitor.notifications.model.slack.button;

public class ButtonOption {
    /*
                  "text": "13",
              "value": "18766416543808"
     */
    private String text;
    private String value;

    public ButtonOption(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
