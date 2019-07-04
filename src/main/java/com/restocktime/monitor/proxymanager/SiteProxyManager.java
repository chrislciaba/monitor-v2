package com.restocktime.monitor.proxymanager;

import java.util.ArrayList;
import java.util.List;

public class SiteProxyManager {
    private Integer currentIndex;
    private Integer numProxies;
    private List<List<String>> proxyLists;

    public SiteProxyManager(List<String> proxies, Integer listSize) {
        this.currentIndex = 0;
        this.numProxies = proxies.size();
        proxyLists = new ArrayList<>();
        int idx = 0;

        while((idx + 1) * listSize <= numProxies){
            proxyLists.add(new ArrayList<>(proxies.subList(idx * listSize, (idx + 1) * listSize)));
            idx++;
        }

        int curIdx = 0;
        for(int i = idx * listSize; i < numProxies; i++){
            proxyLists.get(curIdx).add(proxies.get(i));
            curIdx = (curIdx + 1) % proxyLists.size();
        }
    }

    public List<String> getProxyList(){
        List<String> proxiesToRet = proxyLists.get(currentIndex);
        currentIndex = (currentIndex + 1) % proxyLists.size();

        return proxiesToRet;
    }
}
