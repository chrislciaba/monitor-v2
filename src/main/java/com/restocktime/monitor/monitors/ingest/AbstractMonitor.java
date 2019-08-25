package com.restocktime.monitor.monitors.ingest;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;


public abstract class AbstractMonitor {

    public abstract void run(BasicRequestClient basicRequestClient, boolean isFirst);

    public int getNumUrls(){
        return 1;
    }

    public abstract String getUrl();
}
