package com.restocktime.monitor.bots.naked;

import com.restocktime.monitor.config.NakedLogin;
import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.wrapper.CloudflareRequestWrapper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Naked {
    final static Logger logger = Logger.getLogger(Naked.class);
    private static final String cart = "https://www.nakedcph.com/cart/add";
    private static final String LOGIN = "https://www.nakedcph.com/auth/submit";
    private static final String loginHome = "https://www.nakedcph.com/auth/view";
    private static final String cart_body = "_AntiCsrfToken=%s&id=%s&partial=ajax-cart";

    public static boolean getCartHold(BasicHttpResponse basicHttpResponse1, CloudflareRequestWrapper httpRequestHelper, BasicRequestClient basicRequestClient, List<NakedLogin> logins, int loginIdx, String productName, Notifications notifications){
        if(basicHttpResponse1 == null || basicHttpResponse1.getBody() == null){
            return false;
        }

        String responseString = basicHttpResponse1.getBody().get();


        String sizeStr = "<option\\s+value=\"([0-9]*)\">\\s+([0-9.]*)\\s+</option>";
        Pattern sizePattern = Pattern.compile(sizeStr);

        String csrfStr= "name=\"_AntiCsrfToken\"\\s+value=\"([^\"]*)\">";
        Pattern csrfPattern = Pattern.compile(csrfStr);

        String csrfStr1= "name=\"_AntiCsrfToken\"\\s+value=\"([A-Za-z0-9_.-=]*)\">";
        Pattern csrfPattern1 = Pattern.compile(csrfStr1);
        Matcher sizeMatcher = sizePattern.matcher(responseString);
        Matcher csrfMatcher = csrfPattern.matcher(responseString);
        if(sizeMatcher.find() && csrfMatcher.find()){
            String sizeAvailable = sizeMatcher.group(2);
            String sizeSku = sizeMatcher.group(1);
            String csrfToken = csrfMatcher.group(1);
            String body = String.format(cart_body, csrfToken, sizeSku);
            logger.info(body);

            List<Header> headers = new ArrayList<>();
            headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8"));
            headers.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
            basicRequestClient.getHeaderList().addAll(headers);
            BasicHttpResponse basicHttpResponse =
                    httpRequestHelper.performPost(basicRequestClient, cart, body);
            logger.info(basicHttpResponse.getBody());
            if(basicHttpResponse != null && basicHttpResponse.getBody() != null && basicHttpResponse.getBody().get().contains("Your shopping bag")){
                BasicHttpResponse response = httpRequestHelper.performGet(basicRequestClient, loginHome);

                if(response != null && response.getBody() != null){
                    String responseStr1 = response.getBody().toString();
                    Matcher csrf2 = csrfPattern1.matcher(responseStr1);
                    String username = logins.get(loginIdx).getEmail();
                    String pass = logins.get(loginIdx).getPassword();
                    loginIdx = (loginIdx + 1) % logins.size();
                    if(csrf2.find()){
                        String csrfStringLogin = csrf2.group(1);
                        logger.info(csrfStringLogin);
                        logger.info(csrf2.group(0));
                        logger.info("");

                        String loginBody;
                        try {
                            loginBody = "_AntiCsrfToken=" + csrfStringLogin + "&email=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(pass, "UTF-8") + "&action=login";
                            logger.info(loginBody);
                            Thread.sleep(2000);
                        } catch(Exception e){

                            return false;
                        }

                        BasicHttpResponse loginResp =
                                httpRequestHelper.performPost(basicRequestClient, LOGIN, loginBody);
                        logger.info(loginResp.getBody());

                        //AttachmentCreater attachmentCreater1 = new AttachmentCreater();
                        //attachmentCreater1.addMessages("https://www.nakedcph.com/auth/view", productName + " CART SIZE " + sizeAvailable, " user: " + username +", pass: " + pass, null, null);

                        //Notifications.send(attachmentCreater1);
                        return true;
                    }
                }

            }
        }

        return false;
    }
}
