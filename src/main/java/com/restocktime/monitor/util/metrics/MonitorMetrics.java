package com.restocktime.monitor.util.metrics;

import com.restocktime.monitor.util.log.DiscordLog;

public class MonitorMetrics {
    private int success;
    private int failures;
    private int bans;
    private long startTime;
    private String name;
    private DiscordLog discordLog;

    public MonitorMetrics(String name) {
        this.success = 0;
        this.failures = 0;
        this.bans = 0;
        this.startTime = System.currentTimeMillis();
        this.name = name;
        this.discordLog = new DiscordLog();
    }

    public void error() {
        failures++;
        checkTotal();
    }

    public void success() {
        success++;
        checkTotal();
    }

    public void ban() {
        bans++;
        checkTotal();
    }

    private void checkTotal(){
        if (failures + success == 5) {
            DiscordLog.log(name + " (Errors=" + failures + ", Successes=" + success + ", Bans=" + bans +")");
            failures = 0;
            success = 0;
            bans = 0;
        }
    }
}
