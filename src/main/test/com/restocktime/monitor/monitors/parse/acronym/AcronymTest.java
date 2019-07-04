package com.restocktime.monitor.monitors.parse.acronym;

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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

public class AcronymTest {
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private Notifications notifications;
    private AcronymParser acronymParseResponse;
    private BasicHttpResponse basicHttpResponse;
    private BasicRequestClient basicRequestClient;

    @Before
    public void setup(){
        httpRequestHelper = mock(HttpRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);
        notifications = mock(Notifications.class);
        acronymParseResponse = mock(AcronymParser.class);
        basicHttpResponse = mock(BasicHttpResponse.class);
        basicRequestClient = mock(BasicRequestClient.class);

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);

        when(httpRequestHelper.performGet(basicRequestClient, "https://url.com")).thenReturn(basicHttpResponse);
        s = new SlackObj[0];
        d = new String[0];
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);




      //  acronym = new Acronym("https://url.com", 0, notifications, attachmentCreater, httpRequestHelper, acronymParseResponse);
    }

    @Test
    public void testNotifyTrue(){
        //acronym.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(basicRequestClient, "https://url.com");
        Mockito.verify(acronymParseResponse, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
}
