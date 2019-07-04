package com.restocktime.monitor.monitors.parse.barneys;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class BarneysTest {
   // private Barneys barneys;
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private Notifications notifications;
    private BarneysParseProductAbstractResponse barneysParseProductResponse;
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
        barneysParseProductResponse = mock(BarneysParseProductAbstractResponse.class);
        basicHttpResponse = mock(BasicHttpResponse.class);

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com"))).thenReturn(basicHttpResponse);
        s = new SlackObj[0];
        d = new String[0];
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);




      //  barneys = new Barneys("https://url.com", 0, notifications, attachmentCreater, httpRequestHelper, barneysParseProductResponse);
    }

    @Test
    public void testNotifyTrue(){
      //  barneys.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com"));
        Mockito.verify(barneysParseProductResponse, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
}
