package com.restocktime.monitor.util.url;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class UrlHelper {

    public static String deriveBaseUrl(String url){
        try {
            URL getBaseFromurl = new URL(url);
            return getBaseFromurl.getProtocol() + "://" + getBaseFromurl.getHost();
        } catch(Exception e){
            return null;
        }
    }

    public static String getHost(String url){
        try {
            URL getBaseFromurl = new URL(url);
            return getBaseFromurl.getHost();
        } catch(Exception e){
            return null;
        }
    }

    public static String urlWithRandParam(String url){
        long generatedLong = Math.abs(new Random().nextLong());
        long generatedLong2 =  Math.abs(new Random().nextLong());
        if(url.contains("?")){
            return url + "&" + Long.toString(generatedLong2) + "=" + Long.toString(generatedLong);
        } else {
            return url + "?" + Long.toString(generatedLong2) + "=" + Long.toString(generatedLong);
        }
    }

    public static String urlRandNumberAppended(String url){
        long generatedLong = new Random().nextLong();
        if(url.contains("?")){
            return url + "&" + Long.toString(generatedLong);
        } else {
            return url + "?" + Long.toString(generatedLong);
        }
    }

    public static boolean isValidUrl(String urlStr){
        try{
            URL url = new URL(urlStr);
            return true;
        } catch(MalformedURLException e){
            return false;
        }
    }


}
