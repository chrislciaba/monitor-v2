package com.restocktime.monitor.util.captcha.twocap;

import com.restocktime.monitor.util.ops.log.DiscordLog;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;
import static com.restocktime.monitor.util.ops.log.WebhookType.CF;


public class TwoCaptchaService {

    final static Logger logger = Logger.getLogger(TwoCaptchaService.class);


    /**
     * This class is used to establish a connection to 2captcha.com
     * and receive the token for solving google recaptcha v2
     *
     * @author Chillivanilli
     * @version 1.0
     *
     * If you have a custom software requests, please contact me
     * via forum: http://thebot.net/members/chillivanilli.174861/
     * via eMail: chillivanilli@chillibots.com
     * via skype: ktlotzek
     */


    /**
     * Your 2captcha.com captcha KEY
     */
    private String apiKey;


    /**
     * The google site key from the page you want to solve the recaptcha at
     */
    private String googleKey;


    /**
     * The URL where the recaptcha is placed.
     * For example: https://www.google.com/recaptcha/api2/demo
     */
    private String pageUrl;

    /**
     * The proxy ip if you want a worker to solve the recaptcha through your proxy
     */
    private String proxyIp;

    /**
     * The proxy port
     */
    private String proxyPort;

    /**
     * Your proxy username, if your proxy uses user authentication
     */
    private String proxyUser;

    /**
     * Your proxy password, if your proxy uses user authentication
     */
    private String proxyPw;

    private CloseableHttpClient closeableHttpClient;



    /**
     * Constructor if you don't use any proxy
     * @param apiKey
     * @param googleKey
     * @param pageUrl
     */
    public TwoCaptchaService(String apiKey, String googleKey, String pageUrl, CloseableHttpClient closeableHttpClient) {
        this.apiKey = apiKey;
        this.googleKey = googleKey;
        this.pageUrl = pageUrl;
        this.closeableHttpClient = closeableHttpClient;
    }

    protected String performGet(String url){
        HttpGet request = new HttpGet(url);
        String responseString = "";
        try {
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            HttpResponse response = closeableHttpClient.execute(request);
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
        } catch(Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        } finally {
            request.releaseConnection();
        }
        return responseString;
    }


    /**
     * Sends the recaptcha challenge to 2captcha.com and
     * checks every second if a worker has solved it
     *
     * @return The response-token which is needed to solve and submit the recaptcha
     * @throws InterruptedException, when thread.sleep is interrupted
     * @throws IOException, when there is any server issue and the request cannot be completed
     */
    public String solveCaptcha() throws InterruptedException {
        long t0 = System.currentTimeMillis();
        String parameters = "key=" + apiKey
                + "&method=userrecaptcha"
                + "&googlekey=" + googleKey
                + "&pageurl=" + pageUrl.split("\\?")[0];

        String responseStr = performGet("http://2captcha.com/in.php?" + parameters);
        logger.info(responseStr);
        String captchaId = responseStr.replaceAll("\\D", "");
        int timeCounter = 0;

        do {
            responseStr = performGet("http://2captcha.com/res.php?key=" + apiKey
                    + "&action=get"
                    + "&id=" + captchaId);

            Thread.sleep(5000);
            logger.info(responseStr);
            timeCounter++;
        } while(responseStr.contains("NOT_READY"));

        String gRecaptchaResponse = responseStr.replaceAll("OK\\|", "").replaceAll("\\n", "");
        long t1 = System.currentTimeMillis();
        DiscordLog.log(CF,"Took " + ((t1-t0)/1000) + " seconds to get captcha");
        return gRecaptchaResponse;
    }

    /**
     *
     * @return The 2captcha.com captcha key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the 2captcha.com captcha key
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     *
     * @return The google site key
     */
    public String getGoogleKey() {
        return googleKey;
    }

    /**
     * Sets the google site key
     * @param googleKey
     */
    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    /**
     *
     * @return The page url
     */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * Sets the page url
     * @param pageUrl
     */
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     *
     * @return The proxy ip
     */
    public String getProxyIp() {
        return proxyIp;
    }

    /**
     * Sets the proxy ip
     * @param proxyIp
     */
    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    /**
     *
     * @return The proxy port
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets the proxy port
     * @param proxyPort
     */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     *
     * @return The proxy authentication user
     */
    public String getProxyUser() {
        return proxyUser;
    }

    /**
     * Sets the proxy authentication user
     * @param proxyUser
     */
    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    /**
     *
     * @return The proxy authentication password
     */
    public String getProxyPw() {
        return proxyPw;
    }

    /**
     * Sets the proxy authentication password
     * @param proxyPw
     */
    public void setProxyPw(String proxyPw) {
        this.proxyPw = proxyPw;
    }
}
