package com.restocktime.monitor.util.http.request.headers.useragent;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Random;

public class GoogleChromeUserAgentGenerator {
    private static final String USER_AGENT_TEMPLATE = "Mozilla/5.0 (Macintosh; Intel Mac OS X %s) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/%s Safari/537.36";

    private static final List<String> CHROME_VERSIONS = ImmutableList.of(
            "76.0.3809.102",
            "76.0.3809.87",
            "75.0.3770.100",
            "75.0.3770.80",
            "74.0.3729.108",
            "73.0.3683.75",
            "73.0.3683.86",
            "72.0.3626.121",
            "72.0.3626.96",
            "71.0.3578.98",
            "71.0.3578.80",
            "70.0.3538.113",
            "70.0.3538.77",
            "69.0.3497.81",
            "68.0.3440.75",
            "67.0.3396.62",
            "66.0.3359.117",
            "65.0.3325.146",
            "64.0.3282.167",
            "63.0.3239.84",
            "62.0.3202.62",
            "61.0.3163.113",
            "60.0.3112.113"
    );

    private static final List<String> MAC_OS_VERSION = ImmutableList.of(
            "10_14_5",
            "10_14_4",
            "10_14_3",
            "10_14_2",
            "10_14_1",
            "10_14_0",
            "10_13_6",
            "10_13_5",
            "10_13_4",
            "10_13_3",
            "10_13_2",
            "10_13_1",
            "10_13_0",
            "10_12_6",
            "10_12_5",
            "10_12_4",
            "10_12_3",
            "10_12_2",
            "10_12_1",
            "10_12_0"
    );

    public static String generateUserAgent() {
        Random random = new Random();
        return String.format(USER_AGENT_TEMPLATE, MAC_OS_VERSION.get(random.nextInt(MAC_OS_VERSION.size())), CHROME_VERSIONS.get(random.nextInt(CHROME_VERSIONS.size())));
    }
}
