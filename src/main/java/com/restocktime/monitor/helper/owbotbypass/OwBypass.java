package com.restocktime.monitor.helper.owbotbypass;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.timeout.Timeout;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.regex.Pattern;

public class OwBypass {
    private Pattern uidPattern = Pattern.compile("ipp_uid=([^;]*)");
    private Pattern uid1Pattern = Pattern.compile("ipp_uid1=([^;]*)");
    private Pattern uid2Pattern = Pattern.compile("ipp_uid2=([^;]*)");

    public BasicHttpResponse owBypass(){
        /*List<String> prox = new ArrayList<>();
        prox.add("38.109.173.112:3128:benyakar_isaac_gmail_com:JzFgvcdWq7");
        ClientBuilder c = new ClientBuilder();
        BasicRequestClient x = c.buildClients("https://www.off---white.com", prox).get(0);
        String[] ke = {"12334"};
        CloudflareRequestHelper cloudflareRequestHelper = new CloudflareRequestHelper(ke);
        String url = "https://www.off---white.com";*/


        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        ChromeOptions options = new ChromeOptions();
        ClassLoader classLoader = getClass().getClassLoader();

        options.addExtensions(new File("src/main/resources/Proxy-Auto-Auth_v2.0.crx"));
        options.addArguments("window-size=1500,1500");
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("138.59.7.17:8080");
        options.addArguments("--proxy-server=38.109.173.190:3128");
        WebDriver webDriver = new ChromeDriver(options);//JzFgvcdWq7
        webDriver.findElement(By.id("login")).sendKeys("benyakar_isaac_gmail_com");
        webDriver.findElement(By.id("password")).sendKeys("JzFgvcdWq7");
        webDriver.findElement(By.id("save")).click();
        for(int i = 0; i < 100; i++) {
            webDriver.navigate().to("https://www.off---white.com/api/variants/113300?token=f259ce010ee5d18a33fac200f1d227d46008e93ff5cac423");

            String resp = (String) ((JavascriptExecutor) webDriver).executeScript("return document.body.innerText;");

            Timeout.timeout(5000);
        }

        webDriver.quit();

        return null;

       /* for(int i = 0; i < 11; i++) {

           // BasicHttpResponse b = cloudflareRequestHelper.performGet(x, url);


            String s;
            try {
             //   s = b.getBody();
                System.out.println(s);
                if(s.contains("<title>Off-white</title>")){
                    System.out.println("BYPASS");
                    return b;


                }
                s = s.replaceAll("location\\.href=ipp", "window.cook=ipp").replaceAll("<meta http-equiv=\"refresh\" content=\"[^\"]*\">", "");
                FileUtils.writeStringToFile(new File("/tmp/bot.html"), s);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                long x1 = System.currentTimeMillis();
                webDriver.navigate().to("file:///tmp/bot.html");
                String resp = (String) ((JavascriptExecutor) webDriver).executeScript("return window.cook;");

                System.out.println(resp);
                url = resp;
                long y = System.currentTimeMillis();

                System.out.println((y - x1) / 1000);


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        webDriver.close();

        return null;*/
    }
}
