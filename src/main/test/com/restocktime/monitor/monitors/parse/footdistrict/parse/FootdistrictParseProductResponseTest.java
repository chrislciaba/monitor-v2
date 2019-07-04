package com.restocktime.monitor.monitors.parse.footdistrict.parse;

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

public class FootdistrictParseProductResponseTest {
    private FootdistrictParseProductAbstractResponse footdistrictParseProductResponse;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "<div class=\"add-to-cart\">\n" +
                "               <!-- <label for=\"qty\">Cantidad:</label>\n" +
                "        <input type=\"text\" name=\"qty\" id=\"qty\" maxlength=\"12\" value=\"1\" title=\"Quantity\" class=\"input-text qty\" /> -->\n" +
                "                <button type=\"button\" title=\"Add to cart\" class=\"button btn-cart\" onclick=\"productAddToCartForm.submit(this)\"><span><span>Add to cart</span></span></button>\n" +
                "            </div>\n" +
                "</div>\n" +
                "              \n" +
                "        \t </form>";
        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(true);

   //     footdistrictParseProductResponse = new FootdistrictParseProductAbstractResponse(stockTracker, "https://url.com");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        footdistrictParseProductResponse.parse(basicHttpResponse, attachmentCreater, false);
       // Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://url.com"), eq("https://url.com"), eq("FootDistrict"), any(), any());

    }

    @Test
    public void testOOS(){
        String testInput = "out-of-stock";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        footdistrictParseProductResponse.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(false);
        footdistrictParseProductResponse.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }
}
