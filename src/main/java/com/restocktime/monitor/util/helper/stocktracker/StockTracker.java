package com.restocktime.monitor.util.helper.stocktracker;

import java.util.Map;

public class StockTracker {
    Map<String, Long> sizeTrackerMap;
    int timeout;

    public StockTracker(Map<String, Long> sizeTrackerMap, int timeout){
        this.sizeTrackerMap = sizeTrackerMap;
        this.timeout = timeout;
    }

    public boolean notifyForObject(String obj, boolean isFirst){
        Long timeMilliseconds = System.currentTimeMillis();

        if(!sizeTrackerMap.containsKey(obj) && !isFirst){
            sizeTrackerMap.put(obj, timeMilliseconds + timeout);
            return true;
        }

        sizeTrackerMap.put(obj, timeMilliseconds + timeout);
        return false;
    }

    public void setOOS(String obj){
        if(sizeTrackerMap.containsKey(obj) && timeout != -1 && sizeTrackerMap.get(obj) < System.currentTimeMillis()){
            sizeTrackerMap.remove(obj);
        }
    }

}
