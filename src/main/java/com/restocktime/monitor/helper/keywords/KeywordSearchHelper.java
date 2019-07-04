package com.restocktime.monitor.helper.keywords;

import com.restocktime.monitor.helper.keywords.transformer.NameChangeMapTransformer;
import com.restocktime.monitor.helper.keywords.transformer.StringToSearchTransformer;

import java.util.*;

import static java.lang.System.exit;

public class KeywordSearchHelper {
    private boolean isKeyword;
    private boolean yesToAll;
    private List<Map<String, Boolean>> keywordModel;

    public KeywordSearchHelper(String keywordParseString){
        if(keywordParseString.equals("")){
            yesToAll = true;
        } else {
            yesToAll = false;
            this.isKeyword = true;
            keywordModel = StringToSearchTransformer.stringToKeywordModel(keywordParseString);
        }

    }

    public KeywordSearchHelper(Map<String, String> urlToNamesMap){
        this.isKeyword = false;
        keywordModel = NameChangeMapTransformer.stringToKeywordModel(urlToNamesMap);
    }

    public KeywordSearchHelper(KeywordSearchHelper keywordSearchHelper){
        this.isKeyword = keywordSearchHelper.isKeyword;
        this.keywordModel = keywordSearchHelper.keywordModel;
    }

    public boolean search(String input){

        if(input == null || input.length() == 0){
            return false;
        }

        if(yesToAll){
            return true;
        }

        if(isKeyword) {
            String sanitized = input.replaceAll("[^a-zA-Z0-9\\-\\s]", "").replaceAll("\\s+", " ").toLowerCase().trim();
            List<String> keywords = Arrays.asList(sanitized.split("\\s+"));
            boolean positiveMatch;
            boolean foundOne;

            for (Map<String, Boolean> searchMap : keywordModel) {
                positiveMatch = true;
                foundOne = false;
                int countOfPositive = 0;
                String kw = "";
                for(String key : searchMap.keySet()){
                    countOfPositive += (searchMap.get(key))?1:0;
                    kw = kw + ";" + ((searchMap.get(key))?"+":"-") + key;
                }

                int countOfPositiveFound = 0;

                Map<String, Boolean> found = new HashMap<>();

                for (String keyword : keywords) {
                    if (searchMap.containsKey(keyword)) {
                        foundOne = true;
                        positiveMatch = positiveMatch && searchMap.get(keyword);
                        if(!found.containsKey(keyword)){
                            countOfPositiveFound += (searchMap.get(keyword))?1:0;
                        }
                        found.put(keyword, true);

                    }
                }



                if (positiveMatch && foundOne && countOfPositive == countOfPositiveFound) {
                    return true;
                }
            }

            return false;
        } else {
            for (Map<String, Boolean> searchMap : keywordModel) {
                if(searchMap.containsKey(input)){
                    return true;
                }
            }
            return false;
        }
    }

    public String searchAndReturnMatch(String input){

        if(input == null || input.length() == 0){
            return null;
        }

        if(yesToAll){
            return "";
        }

        if(isKeyword) {
            String sanitized = input.toLowerCase().trim();
            List<String> keywords = Arrays.asList(sanitized.split("\\s+"));
            boolean positiveMatch;
            boolean foundOne;

            for (Map<String, Boolean> searchMap : keywordModel) {
                positiveMatch = true;
                foundOne = false;
                int countOfPositive = 0;
                List<String> kwList = new ArrayList<>();
                for(String key : searchMap.keySet()){
                    countOfPositive += (searchMap.get(key))?1:0;
                    kwList.add(((searchMap.get(key)) ? "+" : "-") + key);
                }

                int countOfPositiveFound = 0;

                Map<String, Boolean> found = new HashMap<>();

                for (String keyword : keywords) {
                    if (searchMap.containsKey(keyword)) {
                        foundOne = true;
                        positiveMatch = positiveMatch && searchMap.get(keyword);
                        if(!found.containsKey(keyword)){
                            countOfPositiveFound += (searchMap.get(keyword))?1:0;
                        }
                        found.put(keyword, true);

                    }
                }



                if (positiveMatch && foundOne && countOfPositive == countOfPositiveFound) {
                    return String.join(",", kwList);
                }
                kwList.clear();
            }

            return null;
        } else {
            for (Map<String, Boolean> searchMap : keywordModel) {
                if(searchMap.containsKey(input)){
                    return input;
                }
            }
            return null;
        }
    }

    public void updateKeywords(Map<String, String> urlToNamesMap){
        keywordModel.clear();
        keywordModel = NameChangeMapTransformer.stringToKeywordModel(urlToNamesMap);
    }
}
