package com.restocktime.monitor.monitors.ingest.shopify;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.httprequests.HttpRequestHelper;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.timeout.Timeout;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.parse.shopify.helper.GetAllProducts;
import com.restocktime.monitor.monitors.parse.shopify.helper.GetApiToken;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyProductListingsResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.util.*;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ShopifyProductListings extends AbstractMonitor {


    private static final String PRODUCT_LISTINGS_TEMPLATE = "%s/api/product_listings.json";

    final static Logger log = Logger.getLogger(ShopifyProductListings.class);

    private String url;
    private int delay;
    private String apiToken;
    private List<Header> headers;
    private Map<Long, Boolean> products;


    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private ShopifyProductListingsResponseParser shopifyProductListingsResponseParser;

    public ShopifyProductListings(String url, int delay,  AttachmentCreater attachmentCreater, HttpRequestHelper httpRequestHelper, ShopifyProductListingsResponseParser shopifyProductListingsResponseParser){
        this.url = String.format(PRODUCT_LISTINGS_TEMPLATE, UrlHelper.deriveBaseUrl(url));
        this.delay = delay;
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.shopifyProductListingsResponseParser = shopifyProductListingsResponseParser;
        this.headers = new ArrayList<>();
        this.apiToken = null;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        Timeout.timeout(delay);

        if(apiToken == null){
            apiToken = GetApiToken.getApiToken(httpRequestHelper, basicRequestClient, url);
            if(apiToken == null){
                return;
            }
            String header = "Basic " + new String(Base64.getEncoder().encode(apiToken.getBytes()));
            //logger.info(header);
            headers.add(new BasicHeader("Authorization", header));

            basicRequestClient.setHeaderList(headers);
            products = GetAllProducts.getExistingProductMap(httpRequestHelper, basicRequestClient, url);
            if(products == null){
                return;
            }
            shopifyProductListingsResponseParser.setProducts(products);
        }


        try{
            basicRequestClient.setHeaderList(headers);
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient, url);
            shopifyProductListingsResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }

    }


    public String getUrl(){
        return url;
    }
}
