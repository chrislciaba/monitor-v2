package com.restocktime.monitor.monitors.parse.offwhite;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteProductAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.offwhite.parse.OffWhiteSearchAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlHelper.class)
public class OffWhiteTest {
    private OffWhite offWhite;
    private CloudflareRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private Notifications notifications;
    private OffWhiteProductAbstractResponseParser offWhiteProductResponseParser;
    private OffWhiteSearchAbstractResponseParser offWhiteSearchResponseParser;
    private BasicHttpResponse basicHttpResponse;
    private BasicRequestClient basicRequestClient;


    @Before
    public void setup(){
        basicRequestClient = mock(BasicRequestClient.class);

        httpRequestHelper = mock(CloudflareRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);
        notifications = mock(Notifications.class);
        offWhiteProductResponseParser = mock(OffWhiteProductAbstractResponseParser.class);
        offWhiteSearchResponseParser = mock(OffWhiteSearchAbstractResponseParser.class);
        basicHttpResponse = mock(BasicHttpResponse.class);
        PowerMockito.mockStatic(UrlHelper.class);
        when(UrlHelper.urlWithRandParam("https://url.com")).thenReturn("https://url.com?_=1");
        when(UrlHelper.urlWithRandParam("https://url.com/search?q=hello")).thenReturn("https://url.com/search?q=hello&_=2");



        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);

    }

    @Test
    public void testNotifyProductTrue(){
   //     offWhite = new OffWhite("https://url.com", 0, notifications, attachmentCreater, httpRequestHelper, offWhiteProductResponseParser, offWhiteSearchResponseParser);

        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com?_=1"))).thenReturn(basicHttpResponse);
        offWhite.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com?_=1"));
        Mockito.verify(offWhiteProductResponseParser, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }

    @Test
    public void testNotifySearchTrue(){
      //  offWhite = new OffWhite("https://url.com/search?q=hello", 0, notifications, attachmentCreater, httpRequestHelper, offWhiteProductResponseParser, offWhiteSearchResponseParser);

        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com/search?q=hello&_=2"))).thenReturn(basicHttpResponse);
        offWhite.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com/search?q=hello&_=2"));
        Mockito.verify(offWhiteSearchResponseParser, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }

    /*private CloudflareRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private Map<Integer, Long> viewed;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        httpRequestHelper = mock(CloudflareRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String apiResp = "{\"available_sizes\":[{\"id\":107421,\"name\":\"M\",\"preorder_only\":false}]}";

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(apiResp);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(httpRequestHelper.performGet(eq(closeableHttpClient), eq(requestConfig), any(), eq("https://url.com.json"), any())).thenReturn(basicHttpResponse);
        s = new SlackObj[0];
        d = new String[0];
        PowerMockito.mockStatic(Notifications.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        viewed = new HashMap<>();
        viewed.put(107421, 0L);

        //offWhite = new OffWhite("https://url.com.json", 0, s, d, attachmentCreater, httpRequestHelper, viewed);
    }

    @Test
    public void testRun(){
        offWhite.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://url.com"), eq("TEST NAME"), eq("Off White"), any(), any());
    }

    @Test
    public void testOutOfStock(){
        String apiResp = "{\"available_sizes\":[]}";
        when(basicHttpResponse.getBody()).thenReturn(apiResp);

        offWhite.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testRunTimeout(){
        viewed.put(107421, System.currentTimeMillis() / 1000 - 10);

        offWhite.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testNotRunYet() {
        viewed.clear();
        offWhite.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }*/
}
