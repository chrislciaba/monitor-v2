package com.restocktime.monitor;

import com.restocktime.monitor.helper.password.PasswordHelper;
import com.restocktime.monitor.manager.MonitorManager;

import static java.lang.System.exit;

public class MonitorRunner {

    public static void main(String args[]) throws Exception {



//        HttpGet httpPost = new HttpGet("https://www.sneakersnstuff.com/en/860/mens-nike-sneakers");
//        List<Header> h = new ArrayList<>();
//        h.add(new BasicHeader(HttpHeaders.HOST, "www.sneakersnstuff.com"));
//        h.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));
//        h.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/73.0.3683.68 Mobile/15E148 Safari/605.1"));
//        h.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"));
//        h.add(new BasicHeader(HttpHeaders.REFERER, "https://www.sneakersnstuff.com/en"));
//        h.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
//        h.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.9"));
//        h.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age"));
//        h.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
//
//        CloseableHttpClient closeableHttpClient  = HttpClients.custom()
//                .setRedirectStrategy(new LaxRedirectStrategy())
//
//                .setDefaultHeaders(h)
//                .build();
//        HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
//        System.out.println(httpResponse.getStatusLine().getStatusCode());
//        System.out.println(EntityUtils.toString(httpResponse.getEntity()));

        MonitorManager monitorManager = new MonitorManager();
        monitorManager.run();
    }

}