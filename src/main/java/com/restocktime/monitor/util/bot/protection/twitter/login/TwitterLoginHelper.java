package com.restocktime.monitor.util.bot.protection.twitter.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2.LoginResponse;
import com.restocktime.monitor.util.bot.protection.nikefrontendlogin.NikeLogin;
import com.restocktime.monitor.util.bot.protection.twitter.login.model.Cookie;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class TwitterLoginHelper {


    final static Logger log = Logger.getLogger(NikeLogin.class);


    public static Map<String, String> getLoginFrontend(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1500,1500");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");

        WebDriver webDriver = new ChromeDriver(options);
        try {

            webDriver.navigate().to("https://mobile.twitter.com/cybersole");
            Timeout.timeout(3000);
            String resp = (String)((JavascriptExecutor)webDriver).executeScript("return document.cookie");
            webDriver.close();
            return Arrays.asList(resp.split(";")).stream().map(
                    cookie -> {
                        String[] cookieSplit = cookie.trim().split("=");
                        if(cookieSplit.length != 2 || !(cookieSplit[0].equals("gt") || cookieSplit[0].equals("ct0")))
                            return null;
                        return Cookie.builder()
                                .name(cookieSplit[0])
                                .value(cookieSplit[1])
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(o -> o.getName(), o -> o.getValue()));


        } catch (Exception e){
            e.printStackTrace();
            log.error(EXCEPTION_LOG_MESSAGE, e);
            webDriver.close();


        }

        return null;
    }
}
