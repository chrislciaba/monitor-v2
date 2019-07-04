package com.restocktime.monitor.notifications.model.discord;

public class DiscordField {
    private String name;
    private String value;
    private Boolean inline;

    public DiscordField(String name, String value, Boolean inline) {
        this.name = name.replaceAll("[^\\x00-\\x7F]", "");
        this.value = value.replaceAll("[^\\x00-\\x7F]", "");
        this.inline = inline;
    }

    public DiscordField(DiscordField discordField) {
        this.name = discordField.name;
        this.value = discordField.value;
        this.inline = discordField.inline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getInline() {
        return inline;
    }

    public void setInline(Boolean inline) {
        this.inline = inline;
    }
}
