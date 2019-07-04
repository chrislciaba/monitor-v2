package com.restocktime.monitor.monitors.parse.offwhite.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class OffWhiteProductResponseParserTest {
    private OffWhiteProductAbstractResponseParser offWhiteProductResponseParser;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "{\"available_sizes\":[{\"id\":107421,\"name\":\"M\",\"preorder_only\":false}]}";
        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(true);
        when(stockTracker.notifyForObject("107421", false)).thenReturn(true);


   //     offWhiteProductResponseParser = new OffWhiteProductAbstractResponseParser(stockTracker, "https://url.com", "TEST");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        offWhiteProductResponseParser.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://url.com"), eq("TEST"), eq("Off White"), eq(null), eq(null));

    }

    @Test
    public void testOOS(){
        String testInput = "{\"available_sizes\":[]}";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        offWhiteProductResponseParser.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(false);
        offWhiteProductResponseParser.parse(basicHttpResponse, attachmentCreater, false);
    //    Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }
}
