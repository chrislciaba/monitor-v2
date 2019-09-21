package com.restocktime.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.manager.MonitorManager;
import com.restocktime.monitor.util.captcha.anti.AntiCapService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import static java.lang.System.exit;


public class MonitorRunner {

    public static void main(String args[]) throws Exception {
        MonitorManager monitorManager = new MonitorManager();
        monitorManager.run();
    }

}