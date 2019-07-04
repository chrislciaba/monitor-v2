package com.restocktime.monitor.notifications.model.discord;

import java.util.List;

public class DiscordMessage {
    private List<Embed> embeds;
    private String content;

    public DiscordMessage(List<Embed> embeds) {
        this.embeds = embeds;
    }

    public DiscordMessage(List<Embed> embeds, String content) {
        this.embeds = embeds;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DiscordMessage(String content, List<Embed> embeds) {
        this.embeds = embeds;
    }

    public List<Embed> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(List<Embed> embeds) {
        this.embeds = embeds;
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
      "fields": [
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