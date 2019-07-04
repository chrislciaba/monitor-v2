package com.restocktime.monitor.monitors.parse.bstn;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.bstn.BSTN;
import com.restocktime.monitor.monitors.parse.bstn.parse.BSTNParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.bstn.parse.BSTNParseSearchAbstractResponse;
import com.restocktime.monitor.monitors.parse.bstn.parse.BstnParsePageResponse;
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

public class BstnTest {
    private BSTN bstn;
    private CloudflareRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private Notifications notifications;
    private BSTNParseProductAbstractResponse bstnParseProductResponse;
    private BSTNParseSearchAbstractResponse bstnParseSearchResponse;
    private BstnParsePageResponse bstnParsePageResponse;
    private BasicHttpResponse basicHttpResponse;
    private BasicRequestClient basicRequestClient;


    @Before
    public void setup(){
        basicRequestClient = mock(BasicRequestClient.class);
        bstnParsePageResponse = mock(BstnParsePageResponse.class);
        httpRequestHelper = mock(CloudflareRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);
        notifications = mock(Notifications.class);
        bstnParseProductResponse = mock(BSTNParseProductAbstractResponse.class);
        bstnParseSearchResponse = mock(BSTNParseSearchAbstractResponse.class);
        basicHttpResponse = mock(BasicHttpResponse.class);

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        s = new SlackObj[0];
        d = new String[0];
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);




    }

    @Test
    public void testNotifySearchTrue(){
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com/searchstring"))).thenReturn(basicHttpResponse);

     //   bstn = new BSTN("https://url.com/searchstring", "", 0, notifications, attachmentCreater, httpRequestHelper, bstnParseProductResponse, bstnParseSearchResponse, bstnParsePageResponse);

        bstn.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com/searchstring"));
        Mockito.verify(bstnParseSearchResponse, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }

    @Test
    public void testNotifyProductTrue(){
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com/p/"))).thenReturn(basicHttpResponse);

      //  bstn = new BSTN("https://url.com/p/", "", 0, notifications, attachmentCreater, httpRequestHelper, bstnParseProductResponse, bstnParseSearchResponse,bstnParsePageResponse);
        bstn.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com/p/"));
        Mockito.verify(bstnParseProductResponse, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
}
