package com.restocktime.monitor.notifications.model.discord;

import java.util.List;

public class Embed {
    private String title;
    private String color;
    private String url;
    private Footer footer;
    private String description;
    private List<DiscordField> fields;
    private Thumbnail thumbnail;
    private Author author;

    public Embed(String title, String color, String url, Footer footer, String description, List<DiscordField> discordFields, Author author) {
        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.color = color;
        this.url = url;
        this.footer = footer;
        this.description = description;
        this.fields = discordFields;
        this.author = author;
    }

    public Embed(String title, String color, String url, Footer footer, String description, List<DiscordField> discordFields, Thumbnail thumbnail, Author author) {
        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.color = color;
        this.url = url;
        this.footer = footer;
        this.description = description;
        this.fields = discordFields;
        this.thumbnail = thumbnail;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Footer getFooter() {
        return footer;
    }

    public void setFooter(Footer footer) {
        this.footer = footer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DiscordField> getFields() {
        return fields;
    }

    public void setFields(List<DiscordField> fields) {
        this.fields = fields;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}


/*

{
  "embeds": [
    {
      "title": "ADIDAS CONSORTIUM WOMEN'S SOLAR HU NMD SNEAKERS | BARNEYS NEW YORK",
      "color": 1127128,
      "url": "https://www.barneys.com/product/restock-time-cook-505923396.html",
      "footer": {
        "text": "Restocktime AIO",
        "icon_url": "https://i.imgur.com/fKL31aD.jpg"
      },
      "discordFields": [
        {
          "name": "8.5",
          "value": "[8.5](https://discordapp.com)",
          "inline": true
        },
        {
          "name": "8.5",
          "value": "[8.5](https://discordapp.com)",
          "inline": true
        },
        {
          "name": "8.5",
          "value": "[8.5](https://discordapp.com)",
          "inline": true
        },
        {
          "name": "8.5",
          "value": "[8.5](https://discordapp.com)",
          "inline": false
        }
      ]
    }
  ]
}
 */