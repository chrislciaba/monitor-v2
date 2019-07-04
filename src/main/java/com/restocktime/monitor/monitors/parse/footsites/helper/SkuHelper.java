package com.restocktime.monitor.monitors.parse.footsites.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkuHelper {

    public static String getSku(String url){
        Pattern skuPattern = Pattern.compile("/([^./]*)\\.html");
        Matcher m = skuPattern.matcher(url);
        if(m.find()){
            return m.group(1);
        }

        return null;

    }
}
