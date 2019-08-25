package com.restocktime.monitor.monitors.parse.aio.porter.helper;

import java.util.HashMap;
import java.util.Map;

public class PorterHelper {
    public static Map<String, Map<String, Map<String, String>>> initPorterMap(){
        final String NET_UK = "https://www.net-a-porter.com/gb/en/shoppingbag.nap";
        final String NET_US = "hhttps://www.net-a-porter.com/us/en/shoppingbag.nap";
        final String NET_PROD_US = "https://www.net-a-porter.com/us/en/product/%s";
        final String NET_PROD_UK = "https://www.net-a-porter.com/gb/en/product/%s";
        final String NET_CART_US = "https://www.net-a-porter.com/us/en/api/basket/addsku/%s.json";
        final String NET_CART_UK = "https://www.net-a-porter.com/gb/en/api/basket/addsku/%s.json";
        final String NET_WISH_US = "https://www.net-a-porter.com/us/en/api/wishlist/addsku/%s.json";
        final String NET_WISH_UK = "https://www.net-a-porter.com/gb/en/api/wishlist/addsku/%s.json";
        final String NET_CYBER_US = "https://cybersole.io/dashboard/quicktask?url=Net-A-Porter%20US:";
        final String NET_CYBER_UK = "https://cybersole.io/dashboard/quicktask?url=Net-A-Porter%20EU:";

        final String PORTER_US = "https://www.mrporter.com/en-us/shoppingbag.mrp";
        final String PORTER_UK = "https://www.mrporter.com/en-gb/shoppingbag.mrp";
        final String PORTER_PROD_US = "https://www.mrporter.com/en-us/mens/product/%s";
        final String PORTER_PROD_UK = "https://www.mrporter.com/en-gb/mens/product/%s";
        final String PORTER_CART_US = "https://www.mrporter.com/am/api/basket/addsku/%s.json";
        final String PORTER_CART_UK = "https://www.mrporter.com/intl/api/basket/addsku/%s.json";
        final String PORTER_WISH_US = "https://www.mrporter.com/am/api/wishlist/addsku/%s.json";
        final String PORTER_WISH_UK = "https://www.mrporter.com/intl/api/wishlist/addsku/%s.json";
        final String PORTER_CYBER_US = "https://cybersole.io/dashboard/quicktask?url=Mr%20Porter%20US:";
        final String PORTER_CYBER_UK = "https://cybersole.io/dashboard/quicktask?url=Mr%20Porter%20EU:";

        Map<String, String> netMapUs = new HashMap<>();
        netMapUs.put("CART", NET_US);
        netMapUs.put("PRODUCT", NET_PROD_US);
        netMapUs.put("ATC", NET_CART_US);
        netMapUs.put("WISH", NET_WISH_US);
        netMapUs.put("CYBER", NET_CYBER_US);

        Map<String, String> netMapUk = new HashMap<>();
        netMapUk.put("CART", NET_UK);
        netMapUk.put("PRODUCT", NET_PROD_UK);
        netMapUk.put("ATC", NET_CART_UK);
        netMapUk.put("WISH", NET_WISH_UK);
        netMapUk.put("CYBER", NET_CYBER_UK);


        Map<String, String> porterMapUs = new HashMap<>();
        porterMapUs.put("CART", PORTER_US);
        porterMapUs.put("PRODUCT", PORTER_PROD_US);
        porterMapUs.put("ATC", PORTER_CART_US);
        porterMapUs.put("WISH", PORTER_WISH_US);
        porterMapUs.put("CYBER", PORTER_CYBER_US);

        Map<String, String> porterMapUk = new HashMap<>();
        porterMapUk.put("CART", PORTER_UK);
        porterMapUk.put("PRODUCT", PORTER_PROD_UK);
        porterMapUk.put("ATC", PORTER_CART_UK);
        porterMapUk.put("WISH", PORTER_WISH_UK);
        porterMapUk.put("CYBER", PORTER_CYBER_UK);

        Map<String, Map<String, String>> porterMap = new HashMap<>();
        porterMap.put("US", porterMapUs);
        porterMap.put("UK", porterMapUk);

        Map<String, Map<String, String>> netMap = new HashMap<>();
        netMap.put("US", netMapUs);
        netMap.put("UK", netMapUk);

        Map<String, Map<String, Map<String, String>>> ALL_URLS = new HashMap<>();
        ALL_URLS.put("NET", netMap);
        ALL_URLS.put("PORTER", porterMap);
        return ALL_URLS;
    }

    public static String getKey(String url){
        if(url.contains("addsku")) {
            return url.contains("net-a-porter") ? "NET" : "PORTER";
        } else if(url.contains("detail")){
            return url.contains("NAP") ? "NET" : "PORTER";
        }

        return null;
    }
}
