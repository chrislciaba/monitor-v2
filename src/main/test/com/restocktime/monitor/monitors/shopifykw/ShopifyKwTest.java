package com.restocktime.monitor.monitors.shopifykw;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.shopify.ShopifyKw;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyKwAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Notifications.class)
public class ShopifyKwTest {
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private Map<String, Long> stringIntegerMap;
    private ShopifyKw shopifyKw;
    private Notifications notifications;
    private BasicRequestClient basicRequestClient;


    private ShopifyKwAbstractResponseParser shopifyKwResponseParser;
    private BasicHttpResponse basicHttpResponse;
    private StockTracker stockTracker;


    @Before
    public void setup(){
        basicRequestClient = mock(BasicRequestClient.class);

        httpRequestHelper = mock(HttpRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);
        notifications = mock(Notifications.class);
        shopifyKwResponseParser = mock(ShopifyKwAbstractResponseParser.class);
        basicHttpResponse = mock(BasicHttpResponse.class);

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);

      //  shopifyKw = new ShopifyKw("https://url.com", 0, notifications, attachmentCreater, httpRequestHelper, shopifyKwResponseParser);
    }

    @Test
    public void testNotifySearchTrue(){
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com"))).thenReturn(basicHttpResponse);
        shopifyKw.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com"));
        Mockito.verify(shopifyKwResponseParser, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
}
