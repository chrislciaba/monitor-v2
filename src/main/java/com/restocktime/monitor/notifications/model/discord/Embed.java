package com.restocktime.monitor.notifications.model.discord;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Embed {
    private String title;
    private String color;
    private String url;
    private Footer footer;
    private String description;
    private List<DiscordField> fields;
    private Thumbnail thumbnail;
    private Author author;
    private Image image;

    public Embed(String title, String color, String url, Footer footer, String description, List<DiscordField> discordFields, Author author) {
        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.color = color;
        this.url = url;
        this.footer = footer;
        this.description = description;
        this.fields = discordFields;
        this.author = author;
    }

    public Embed(String title, String color, String url, Footer footer, String description, List<DiscordField> discordFields, Thumbnail thumbnail, Author author, Image image) {
        this.title = title.replaceAll("[^\\x00-\\x7F]", "");
        this.color = color;
        this.url = url;
        this.footer = footer;
        this.description = description;
        this.fields = discordFields;
        this.thumbnail = thumbnail;
        this.author = author;
        this.image = image;
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