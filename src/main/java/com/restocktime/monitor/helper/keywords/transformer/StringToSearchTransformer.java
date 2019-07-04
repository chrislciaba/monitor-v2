package com.restocktime.monitor.helper.keywords.transformer;

import java.util.*;

public class StringToSearchTransformer {

    public static List<Map<String, Boolean>> stringToKeywordModel(String input){
        List<String> searches = Arrays.asList(input.toLowerCase().split(";"));
        List<Map<String, Boolean>> singleSearches = new ArrayList<>();
        for(String search: searches){
            List<String> subSearch = Arrays.asList(search.split(","));
            Map<String, Boolean> keywordsSingle = new HashMap<>();
            for(String searchToken : subSearch){
                if(searchToken == null || searchToken.length() <= 1){
                    continue;
                }

                if(searchToken.charAt(0) == '+'){
                    keywordsSingle.put(searchToken.substring(1, searchToken.length()), true);
                } else {
                    keywordsSingle.put(searchToken.substring(1, searchToken.length()), false);
                }
            }

            singleSearches.add(keywordsSingle);
        }

        return singleSearches;
    }
}
