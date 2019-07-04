package com.restocktime.monitor.monitors.parse.nittygritty;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class NittyGrittyTest {
   // private NittyGritty nittyGritty;
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private Notifications notifications;
    private NittyGrittyAbstractResponseParser nittyGrittyResponseParser;
    private BasicHttpResponse basicHttpResponse;
    private BasicRequestClient basicRequestClient;


    @Before
    public void setup(){
        basicRequestClient = mock(BasicRequestClient.class);

        httpRequestHelper = mock(HttpRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);
        notifications = mock(Notifications.class);
        nittyGrittyResponseParser = mock(NittyGrittyAbstractResponseParser.class);
        basicHttpResponse = mock(BasicHttpResponse.class);

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);

     //   nittyGritty = new NittyGritty("https://url.com", 0, notifications, attachmentCreater, httpRequestHelper, nittyGrittyResponseParser);
    }

    @Test
    public void testNotifySearchTrue(){
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com"))).thenReturn(basicHttpResponse);
    //    nittyGritty.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com"));
        Mockito.verify(nittyGrittyResponseParser, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
}
