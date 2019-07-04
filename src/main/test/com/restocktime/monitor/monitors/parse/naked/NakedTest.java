package com.restocktime.monitor.monitors.parse.naked;

import com.restocktime.monitor.config.NakedLogin;
import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
//@PrepareForTest({Notifications.class, Naked.class})
public class NakedTest {
    /*
    private com.restocktime.monitor.monitors.parse.naked.Naked naked;
    private CloudflareRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private Map<String, Long> viewed;
    private BasicHttpResponse basicHttpResponse;
    private Notifications notifications;
    private ParseNakedAbstractResponse parseNakedResponse;

    @Before
    public void setup(){
        httpRequestHelper = mock(CloudflareRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String apiResp = "<title>Nike Sportswear Undercover x Nike React Element 87 BQ2718 400 | Lakeside/Electric Yellow | Footwear - Naked</title> <form x=\"/cart/add\"";

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(apiResp);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(httpRequestHelper.performGet(eq(closeableHttpClient), eq(requestConfig), any(), eq("https://url.com"), any())).thenReturn(basicHttpResponse);
        s = new SlackObj[0];
        d = new String[0];
        PowerMockito.mockStatic(Notifications.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        viewed = new HashMap<>();
        viewed.put("1234", 0L);
        List<NakedLogin> logins = new ArrayList();
        logins.add(mock(NakedLogin.class));
        notifications = mock(Notifications.class);
        parseNakedResponse = mock(ParseNakedAbstractResponse.class);

        naked = new com.restocktime.monitor.monitors.parse.naked.Naked("https://url.com", 0, notifications,  logins, attachmentCreater, httpRequestHelper, parseNakedResponse);
    }

    */
  //  private Naked naked;
    private CloudflareRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private Notifications notifications;
    private ParseNakedAbstractResponse parseNakedResponse;
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
        parseNakedResponse = mock(ParseNakedAbstractResponse.class);
        basicHttpResponse = mock(BasicHttpResponse.class);

        List<NakedLogin> logins = new ArrayList();
        logins.add(mock(NakedLogin.class));

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);

      //  naked = new Naked("https://url.com", 0, notifications, logins, attachmentCreater, httpRequestHelper, parseNakedResponse);
    }

    @Test
    public void testNotifySearchTrue(){
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com"))).thenReturn(basicHttpResponse);
  //      naked.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com"));
        Mockito.verify(parseNakedResponse, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
}
