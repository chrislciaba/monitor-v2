package com.restocktime.monitor.monitors.parse.footdistrict.helper;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import org.apache.http.message.BasicHeader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotBypass {

    Map<String, String> cookieList;


    private static final String scriptPatternStr= "<script>(.*)</script>";
    private Pattern scriptPattern;

    public BotBypass(){
        scriptPattern = Pattern.compile(scriptPatternStr);
        cookieList = new HashMap<>();
    }

    public BasicHttpResponse bypassBotProtection(
            HttpRequestHelper httpRequestHelper,
            BasicRequestClient basicRequestClient,
            BasicHttpResponse basicHttpResponse,
            String url){

        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return basicHttpResponse;
        }

        String responseString = basicHttpResponse.getBody().get();
        try {
            if (responseString.contains("sucuri_cloudproxy_js")) {
                Matcher m = scriptPattern.matcher(responseString);
                if (m.find()) {
                    String exec = "var noop = function () {};document={}; location={reload:noop};" + m.group(1) + "x = (document.cookie);";
                    ScriptEngineManager factory = new ScriptEngineManager();
                    ScriptEngine engine = factory.getEngineByName("JavaScript");
                    String s = engine.eval(exec).toString();

                    basicRequestClient.getHeaderList().add(new BasicHeader("Cookie", s));
                    BasicHttpResponse basicHttpResponse1 = httpRequestHelper.performGet(basicRequestClient, url);
                    cookieList.put(basicRequestClient.getHttpHost().toHostString(), s);

                    return basicHttpResponse1;
                }
            } else {
                return basicHttpResponse;
            }
        } catch(Exception e){

        }

        return null;
    }
}
