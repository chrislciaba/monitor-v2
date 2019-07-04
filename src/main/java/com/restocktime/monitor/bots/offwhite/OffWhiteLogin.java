package com.restocktime.monitor.bots.offwhite;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.bots.profiles.Login;
import org.apache.log4j.Logger;

public class OffWhiteLogin {

    private Login login;

    public OffWhiteLogin(Login login) {
        this.login = login;
    }

    final static Logger logger = Logger.getLogger(OffWhiteLogin.class);


    private final String LOGIN_BODY_TEMPLATE = "utf8=%E2%9C%93&authenticity_token=&spree_user%5Bemail%5D=%s&spree_user%5Bpassword%5D=%s&spree_user%5Bremember_me%5D=0&commit=Login";
    private final String LOGIN_URL_TEMPLATE = "https://www.off---white.com/en/%s/login";

    public void login(BasicRequestClient basicRequestClient, CloudflareRequestHelper cloudflareRequestHelper, String region){
        String loginUrl = String.format(LOGIN_BODY_TEMPLATE, region);
        String loginBody = String.format(LOGIN_URL_TEMPLATE, login.getUsername(), login.getPassword());

        logger.info(loginBody);

        while(true){
            BasicHttpResponse basicHttpResponse = cloudflareRequestHelper.performPost(basicRequestClient, loginUrl, loginBody);
            logger.info(basicHttpResponse.getBody());
            if(basicHttpResponse == null ||  basicHttpResponse.getBody() == null){

                continue;
            } else if(basicHttpResponse.getResponseCode() >= 400){
                logger.info("Site error or cloudflare");
            } else if(basicHttpResponse.getBody().contains("<title>Login - OffWhite</title>")){
                logger.info("Wrong pass");
            } else if(basicHttpResponse.getBody().contains("<title>Off-white</title>")){
                logger.info("Successfully logged in");
                break;
            }
        }
    }
}
