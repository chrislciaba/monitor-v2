package com.restocktime.monitor.monitors.parse.ys.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.password.PasswordHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.attachment.ShopifyBuilder;
import com.restocktime.monitor.monitors.parse.shopify.model.shopify.Variant;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.ys.model.YsFeatured;
import com.restocktime.monitor.monitors.parse.ys.model.YsProduct;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.monitors.parse.shopify.attachment.ShopifyBuilder.buildAttachments;

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
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
            return;
        }

        String baseUrl = UrlHelper.deriveBaseUrl(url);
        Matcher urlMatcher = Pattern.compile(actualUrl).matcher(basicHttpResponse.getBody());
        if(urlMatcher.find()){
            baseUrl = UrlHelper.deriveBaseUrl(urlMatcher.group(1));
        }

        isPassUp = PasswordHelper.getPassStatus(attachmentCreater, basicHttpResponse, baseUrl, isFirst, isPassUp, formatNames);

        Matcher m = productPattern.matcher(basicHttpResponse.getBody());


        while(m.find()){
            shopifyAbstractResponseParser.parse(new BasicHttpResponse(m.group(1), 200), attachmentCreater, isFirst);
        }
    }
}
