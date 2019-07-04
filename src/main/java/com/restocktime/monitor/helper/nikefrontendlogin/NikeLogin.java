package com.restocktime.monitor.helper.nikefrontendlogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv2.LoginResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class NikeLogin {

    public static String getLoginFrontend(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriverLinux");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1500,1500");

        WebDriver webDriver = new ChromeDriver(options);
        try {

            webDriver.navigate().to("https://nike.com/launch");
            webDriver.findElement(By.className("join-log-in")).click();

            webDriver.findElement(By.name("emailAddress")).sendKeys("IvanHarmon9578@gmail.com");

            webDriver.findElement(By.name("password")).sendKeys("IvanHarmon275");

            webDriver.findElement(By.className("nike-unite-submit-button")).click();
            Timeout.timeout(6000);

            webDriver.navigate().to("https://unite.nike.com/session.html");
            Timeout.timeout(3000);
            String resp = (String)((JavascriptExecutor)webDriver).executeScript("return window.localStorage.getItem('com.nike.commerce.snkrs.web.credential');");
            webDriver.close();

            ObjectMapper objectMapper = new ObjectMapper();

            LoginResponse loginResponse = objectMapper.readValue(resp, LoginResponse.class);
            return loginResponse.getAccess_token();


        } catch (Exception e){
            webDriver.close();


        }

        return null;
    }
}
