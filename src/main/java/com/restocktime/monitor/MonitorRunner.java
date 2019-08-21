package com.restocktime.monitor;

import com.restocktime.monitor.manager.MonitorManager;
import com.restocktime.monitor.notifications.client.Discord;
import com.restocktime.monitor.util.hawk.Hawk;
import com.restocktime.monitor.util.log.DiscordLog;

public class MonitorRunner {

    public static void main(String args[]) throws Exception {
        //Hawk h = new Hawk();
        //h.call();

        MonitorManager monitorManager = new MonitorManager();
        monitorManager.run();
    }

}