package com.restocktime.monitor.helper.httprequests.exception;

public class MonitorRequestException extends RuntimeException {

    public MonitorRequestException(String message) {
        super(message);
    }

    public MonitorRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
