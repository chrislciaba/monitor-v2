package com.restocktime.monitor.monitors.parse.footdistrict;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.footdistrict.FootDistrict;
import com.restocktime.monitor.monitors.parse.footdistrict.helper.BotBypass;
import com.restocktime.monitor.monitors.parse.footdistrict.parse.FootdistrictParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.footdistrict.parse.FootdistrictParseSearchAbstractResponse;
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

public class FootdistricTest {
    private FootDistrict footDistrict;
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private Notifications notifications;
    private FootdistrictParseProductAbstractResponse footdistrictParseProductResponse;
    private FootdistrictParseSearchAbstractResponse footdistrictParseSearchResponse;
    private BasicHttpResponse basicHttpResponse;
    private BasicHttpResponse basicHttpResponse1;
    private BotBypass botBypass;
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
        basicHttpResponse = mock(BasicHttpResponse.class);
        basicHttpResponse1 = mock(BasicHttpResponse.class);
        botBypass = mock(BotBypass.class);

        footdistrictParseProductResponse = mock(FootdistrictParseProductAbstractResponse.class);
        footdistrictParseSearchResponse = mock(FootdistrictParseSearchAbstractResponse.class);

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(true);


        when(botBypass.bypassBotProtection(eq(httpRequestHelper), eq(basicRequestClient),eq(basicHttpResponse), eq("https://url.com"))).thenReturn(basicHttpResponse1);

      //  footDistrict = new FootDistrict("https://url.com", 0, notifications, attachmentCreater, httpRequestHelper, botBypass, footdistrictParseProductResponse, footdistrictParseSearchResponse);
    }

    @Test
    public void testNotifySearchTrue(){
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com"))).thenReturn(basicHttpResponse);
        footDistrict.run(basicRequestClient, false);
        Mockito.verify(botBypass, Mockito.times(1)).bypassBotProtection(eq(httpRequestHelper), eq(basicRequestClient),eq(basicHttpResponse), eq("https://url.com"));
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com"));
        Mockito.verify(footdistrictParseProductResponse, Mockito.times(1)).parse(basicHttpResponse1, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
}
