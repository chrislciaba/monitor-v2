package com.restocktime.monitor.notifications.model.slack;

import com.restocktime.monitor.notifications.model.slack.button.ButtonAction;

import java.util.List;

public class Attachment {
    private String text;
    private String author_name;
    private String fallback;
    private String color;
    private String title;
    private String title_link;
    private String image_url;
    private String thumb_url;
    private String footer;
    private String footer_icon;
    private String ts;
    private List<SlackField> fields;
    private String callback_id;
    private List<ButtonAction> actions;

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public List<SlackField> getFields() {
        return fields;
    }

    public void setFields(List<SlackField> fields) {
        this.fields = fields;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getFooter_icon() {
        return footer_icon;
    }

    public void setFooter_icon(String footer_icon) {
        this.footer_icon = footer_icon;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getCallback_id() {
        return callback_id;
    }

    public void setCallback_id(String callback_id) {
        this.callback_id = callback_id;
    }

    public List<ButtonAction> getActions() {
        return actions;
    }

    public void setActions(List<ButtonAction> actions) {
        this.actions = actions;
    }

    public Attachment(String text, String fallback, String color, String title, String title_link, String image_url, String thumb_url, String footer, String footer_icon, String ts, List<SlackField> fields) {
        this.text = text.replaceAll("[^\\x00-\\x7F]", "");
        this.fallback = fallback.replaceAll("[^\\x00-\\x7F]", "");
        this.color = color;
        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.title_link = title_link;
        this.image_url = image_url;
        this.thumb_url = thumb_url;
        this.footer = footer;
        this.footer_icon = footer_icon;
        this.ts = ts;
        this.fields = fields;
    }


    public Attachment(String text, String author_name, String fallback, String color, String title, String title_link, String image_url, String thumb_url, String footer, String footer_icon, String ts, List<SlackField> fields) {
        this.text = text
                .replaceAll("[^\\x00-\\x7F]", "");
        this.author_name = (author_name!= null?(author_name.replaceAll("[^\\x00-\\x7F]", "")):author_name);
        this.fallback = fallback.replaceAll("[^\\x00-\\x7F]", "");
        this.color = color;
        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.title_link = title_link;
        this.image_url = image_url;
        this.thumb_url = thumb_url;
        this.footer = footer;
        this.footer_icon = footer_icon;
        this.ts = ts;
        this.fields = fields;
    }

    public Attachment(String callback_id, String author_name, String footer, String color, List<ButtonAction> actions) {
        this.callback_id = callback_id;
        this.author_name = author_name;
        this.footer = footer;
        this.color = color;
        this.actions = actions;
    }
}