package com.restocktime.monitor.monitors.parse.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.hottopic.ParseHotTopicAbstractResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ParseHotTopicResponseTest {
    private ParseHotTopicAbstractResponse parseHotTopicResponse;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "<input type=\"hidden\" name=\"cartAction\" id=\"cartAction\" value=\"update\" />\n" +
                "<input type=\"hidden\" name=\"pid\" id=\"pid\" value=\"11414285\" />\n" +
                "<div class=\"add-to-cart-container\">\n" +
                "<button id=\"add-to-cart\" type=\"submit\" title=\"Add to Bag\" value=\"Add to Bag\" class=\"button-fancy-large add-to-cart\">Add to Bag</button>\n" +
                "</div>\n" +
                "<div class=\"product-actions\">\n" +
                "<ul class=\"menu\">\n" +
                "<li><a class=\"wl";
        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(true);


     //   parseHotTopicResponse = new ParseHotTopicAbstractResponse(stockTracker, "https://url.com");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        parseHotTopicResponse.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://url.com"), eq("PRODUCT_NAME_UNAVAILABLE"), eq("Hot Topic"), eq(null), eq(null));

    }

    @Test
    public void testOOS(){
        String testInput = "disabled=\"disabled\">Add to Bag";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        parseHotTopicResponse.parse(basicHttpResponse, attachmentCreater, false);
       // Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(false);
        parseHotTopicResponse.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }
}
