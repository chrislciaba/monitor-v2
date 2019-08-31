package com.restocktime.monitor;

import com.restocktime.monitor.manager.MonitorManager;

public class MonitorRunner {

    public static void main(String args[]) throws Exception {
        String s = "<img  data-original=\"https://www.sneakerbarber.com/wp-content/uploads/2019/07/919712-041-2-300x300.jpg\" src=\"https://www.sneakerbarber.com/wp-content/themes/porto/images/lazy.png\" width=\"300\" height=\"300\" class=\"hover-image porto-lazyload\" alt=\"\" /></div>\t\t</a>\n" +
                "\t\t</div>\n" +
                "\n" +
                "\t<div class=\"product-content\">\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\t\t\t<a class=\"product-loop-title\"  href=\"https://www.sneakerbarber.com/en/produkt/air-jordan-11-retro-low-ie-concord/\">\n" +
                "\t<h3 class=\"woocommerce-loop-product__title\">Air Jordan 11 Retro Low IE &#8220;Concord&#8221;</h3>";

        System.out.println(s.replaceAll(">\\s*<", "><"));

        MonitorManager monitorManager = new MonitorManager();
        monitorManager.run();
    }

}