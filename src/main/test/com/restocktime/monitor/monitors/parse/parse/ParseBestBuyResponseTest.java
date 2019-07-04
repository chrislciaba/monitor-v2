package com.restocktime.monitor.monitors.parse.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.parse.bestbuy.BestBuyParseProductAbstractResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ParseBestBuyResponseTest {
    private BestBuyParseProductAbstractResponse bestBuyParseProductResponse;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "ability||result)&&!function(){var i={\"name\":\"priceViewTranslatedDataDux4600000\"};if(i.content=window.jQuery(\"div[data-dux-component=\"+'priceViewTranslatedDataDux4600000'+\"]\").html()||\"\",\"function\"!=typeof window.__duxValidate__){var a=document.createElement(\"script\");a.onload=function(){return window.__duxValidate__(i)},a.src=''+\"/lib-dux/dist/validate-\"+\"a069b97f2596d0f28de17826cac506c6\"+\".js\",document.body.appendChild(a)}else window.__duxValidate__(i)}()}</script><script>window.pdp = window.pdp || {};window.pdp.priceBlock = {buttonState: {\"purchasable\":true,\"buttonState\":\"ADD_TO_CART\",\"displayText\":\"Add to Cart\",\"skuId\":\"4600000\"},price: {\"skuId\":\"4600000\",\"regularPriceMessageType\":\"WAS\",\"regularPrice\":1549.99,\"currentPrice\":1449.99,\"customerPrice\":1449.99,\"instantSavings\":100,\"totalSavings\":100,\"totalSavingsPercent\":6,\"isMAP\":false,\"offerQualifiers\":[{\"offerId\":\"288486\",\"offerName\":\"FREE 6 months of Internet Security with select har";

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(true);


       // bestBuyParseProductResponse = new BestBuyParseProductAbstractResponse(stockTracker, "https://url.com");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        bestBuyParseProductResponse.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages("https://url.com", "PRODUCT_NAME_UNAVAILABLE", "Best Buy", null, null);

    }

    @Test
    public void testOOS(){
        String s = "window.pdp.priceBlock = {buttonState: {\"buttonState\":\"SOLD_OUT\"";
        when(basicHttpResponse.getBody()).thenReturn(s);
        bestBuyParseProductResponse.parse(basicHttpResponse, attachmentCreater, false);
    //    Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());


    }
}
