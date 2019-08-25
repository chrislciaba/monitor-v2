package com.restocktime.monitor.monitors.parse.important.shopify.ys.parse;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.password.PasswordHelper;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.shopify.parse.ShopifyAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YsResponseParser implements AbstractResponseParser {


    private final String productName = "<script class=\"js-product-json\" type=\"application/json\">([^<]*)</script>";
    private final String actualUrl = "<link rel=\"canonical\" href=\"([^\"]*)\">";

    private Pattern productPattern;
    private List<String> formatNames;
    private boolean isPassUp;
    private String url;
    private ShopifyAbstractResponseParser shopifyAbstractResponseParser;


    public YsResponseParser(String url, List<String> formatNames, ShopifyAbstractResponseParser shopifyAbstractResponseParser){
        this.url = url;
        this.productPattern = Pattern.compile(productName);
        this.formatNames = formatNames;
        this.isPassUp = false;
        this.shopifyAbstractResponseParser = shopifyAbstractResponseParser;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String baseUrl = UrlHelper.deriveBaseUrl(url);
        Matcher urlMatcher = Pattern.compile(actualUrl).matcher(basicHttpResponse.getBody().get());
        if(urlMatcher.find()){
            baseUrl = UrlHelper.deriveBaseUrl(urlMatcher.group(1));
        }

        isPassUp = PasswordHelper.getPassStatus(attachmentCreater, basicHttpResponse, baseUrl, isFirst, isPassUp, formatNames);

        Matcher m = productPattern.matcher(basicHttpResponse.getBody().get());


        while(m.find()){
            shopifyAbstractResponseParser.parse(
                    BasicHttpResponse.builder()
                            .body(
                                    Optional.of(m.group(1))
                            )
                            .responseCode(
                                    basicHttpResponse.getResponseCode()
                            ).build(),
                    attachmentCreater,
                    isFirst
            );
        }
    }
}
