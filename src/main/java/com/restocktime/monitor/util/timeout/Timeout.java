package com.restocktime.monitor.util.timeout;

public class Timeout {
    public static void timeout(long millisecs){
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {}
    }
}
