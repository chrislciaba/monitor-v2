package com.restocktime.monitor;

import com.restocktime.monitor.helper.clientbuilder.ClientBuilder;
import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Monitor implements Runnable {

    private Thread t = null;
    private AtomicBoolean running = new AtomicBoolean(false);


    private AbstractMonitor abstractMonitor;
    private List<String> proxyList;
    private String url;
    private Map<String, CloseableHttpClient> clientStore;
    private String site;

    public Monitor(AbstractMonitor abstractMonitor, List<String> proxyList, String url, String site){
        this.abstractMonitor = abstractMonitor;
        this.proxyList = proxyList;
        this.url = url;
        this.clientStore = new HashMap<>();
        this.site = site;
    }


    public void run() {
        try {

            ClientBuilder clientBuilder = new ClientBuilder();
            List<BasicRequestClient> clients = clientBuilder.buildClients(url, proxyList, site);
            Collections.shuffle(clients);

            int proxyIndex = 0;
            for(int i = 0; i < 10; i++) { //run 10x to minimize the random spam
                for (int j = 0; j < abstractMonitor.getNumUrls(); j++) {
                    abstractMonitor.run(clients.get(proxyIndex), true);
                    proxyIndex = (proxyIndex + 1) % clients.size();
                }
            }

            Collections.shuffle(clients);


            while (running.get()) {
                abstractMonitor.run(clients.get(proxyIndex), false);
                proxyIndex = (proxyIndex + 1) % clients.size();
            }
        }catch(Exception e){

        }

    }

    public void start () {
        if (t == null) {
            t = new Thread (this,url);
            running.set(true);
            t.start ();

        }
    }

    public void stop() {
        running.set(false);
        t.interrupt();
    }
}
