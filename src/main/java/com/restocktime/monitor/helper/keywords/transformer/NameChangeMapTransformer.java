package com.restocktime.monitor.helper.keywords.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameChangeMapTransformer {

    public static List<Map<String, Boolean>> stringToKeywordModel(Map<String, String> input){
        List<Map<String, Boolean>> names = new ArrayList<>();
        for(String url : input.keySet()){
            Map<String, Boolean> query = new HashMap<>();
            query.put(input.get(url), true);
            names.add(query);
        }
        return names;
    }
}
