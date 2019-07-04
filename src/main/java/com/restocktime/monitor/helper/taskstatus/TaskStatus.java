package com.restocktime.monitor.helper.taskstatus;

import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.notifications.client.Discord;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.List;

public class TaskStatus {
    private int errors;
    private int success;
    private String id;
    private String url;
    private DiscordLog discordLog;

    public TaskStatus(int errors, int success, String id, String url, DiscordLog discordLog) {
        this.errors = errors;
        this.success = success;
        this.id = id;
        this.url = url;
        this.discordLog = discordLog;
    }

    public void incrementError() {
        errors++;
        checkTotal();
    }
    public void incrementSuccess() {
        success++;
        checkTotal();
    }

    private void checkTotal() {
        if(errors + success < 100) {
            return;
        }

        discordLog.info("Task for url " + url + " had " + errors + " errors and " + success + " successes in the last 100 requests");
        errors = 0;
        success = 0;
    }
}
