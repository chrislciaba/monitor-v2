package com.restocktime.monitor.helper.botstarters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuicktaskConfig {
    private List<String> cookieList;

    public QuicktaskConfig(String people, Map<String, String> cookiesForPeople){
        cookieList = new ArrayList<>();

        if(people == null || cookiesForPeople.keySet().isEmpty()){
            return;
        }
        String[] peopleList = people.split(",");
        for(String key : peopleList){
            cookieList.add(cookiesForPeople.get(key));
        }
    }

    public List<String> getCookieList() {
        return cookieList;
    }

    public void startQts(String url){
        for(String cookie : cookieList){
            CyberStarter.startQuickTask(url, cookie);
        }
    }
}
