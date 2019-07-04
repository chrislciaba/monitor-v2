package com.restocktime.monitor.helper.keywords.helper;

public class KeywordFormatHelper {
    public static String getKeywordString(String defaultKw, String specificKw){
        if(
                (
                        defaultKw.length() == 0 && specificKw.length() == 0
                ) ||
                        specificKw.equals("ALL")
                ){
            return "";
        } else if(defaultKw.length() > 0 && specificKw.length() > 0){
            return defaultKw + ";" + specificKw;
        } else if(defaultKw.length() > 0 && specificKw.length() == 0){
            return  defaultKw;
        } else {
            return specificKw;
        }
    }
}
