package com.restocktime.monitor.util.bot.protection.twitter.login.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
public class Cookie {
    private String name;
    private String value;
}
