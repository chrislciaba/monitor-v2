package com.restocktime.monitor;

import com.restocktime.monitor.manager.MonitorManager;

public class MonitorRunner {

    public static void main(String args[]) throws Exception {

        MonitorManager monitorManager = new MonitorManager();
        monitorManager.run();
    }

}