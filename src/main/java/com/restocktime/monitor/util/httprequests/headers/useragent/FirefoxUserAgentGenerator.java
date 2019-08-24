package com.restocktime.monitor.util.httprequests.headers.useragent;

import java.util.Random;

public class FirefoxUserAgentGenerator {

    private static final String USER_AGENT_TEMPLATE = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.%s; rv:%s.0) Gecko/20100101 Firefox/%s.0";

    public static final String generateUserAgent() {
        Random random = new Random();

        int osVersion = 10 + random.nextInt(5);
        int version = 40 + random.nextInt(29);

        return String.format(USER_AGENT_TEMPLATE, osVersion, version, version);

    }
}
