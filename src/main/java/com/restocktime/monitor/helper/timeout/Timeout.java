package com.restocktime.monitor.helper.timeout;

public class Timeout {
    public static void timeout(long millisecs){
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {}
    }
}
