package com.restocktime.monitor.monitors.parse.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.nittygritty.NittyGrittyAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class NittyGrittyResponseParserTest {
    private NittyGrittyAbstractResponseParser nittyGrittyResponseParser;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "  <div class=\"product-grid__item-wrapper\">\n" +
                "\n" +
                "          <div class=\"product-item\" itemprop=\"offers\" itemscope itemtype=\"http://schema.org/Offer\"><a href=\"/men/footwear/deerupt-runner-cl_-white\" data-js=\"product-item-link\" class=\"product-item__overlay\"></a><a class=\"product-item__link\"\n" +
                "      href=\"/men/footwear/deerupt-runner-cl_-white\"\n" +
                "      data-js=\"product-item-link\"\n" +
                "      data-ecommerce-currency=\"EUR\"\n" +
                "      data-ecommerce-id=\"B41759Cl White\"\n" +
                "      data-ecommerce-name=\"Deerupt Runner\"\n" +
                "      data-ecommerce-category=\"Men / Footwear / Sneakers\"\n" +
                "      data-ecommerce-price=\"120\"\n" +
                "      data-ecommerce-variant=\"Cl White\"\n" +
                "      data-ecommerce-brand=\"adidas Originals\"\n" +
                "      data-ecommerce-list=\"\"><img\n" +
                "        data-sizes=\"auto\"\n" +
                "        src=\"https://nittygritty.centracdn.net/client/dynamic/images/15513_38504526da-_g6a5212-2-thumb.jpg\"\n" +
                "        data-src=\"https://nittygritty.centracdn.net/client/dynamic/images/15513_38504526da-_g6a5212-2.jpg\"\n" +
                "        data-srcset=\"https://nittygritty.centracdn.net/client/dynamic/images/15513_38504526da-_g6a5212-2-thumb.jpg 300w,\n" +
                "        https://nittygritty.centracdn.net/client/dynamic/images/15513_38504526da-_g6a5212-2.jpg 600w\"\n" +
                "        class=\"lazyload product-item__image\"\n" +
                "        alt=\"Deerupt Runner\"\n" +
                "        title=\"Deerupt Runner\" /></a><div class=\"product-item__details\"><p class=\"product-item__brand\">adidas Originals</p><p class=\"product-item__name\"><a href=\"/men/footwear/deerupt-runner-cl_-white\" class=\"product-item__name\" itemprop=\"name\">\n" +
                "          Deerupt Runner\n" +
                "        </a></p><div class=\"price \"><span class=\"price__value\" data-js=\"price-value\">\n" +
                "      120.00 EUR\n" +
                "  </span></div></div></div>\n" +
                "        </div>";
        when(keywordSearchHelper.search("Deerupt Runner")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/men/footwear/deerupt-runner-cl_-white", false)).thenReturn(true);


    //    nittyGrittyResponseParser = new NittyGrittyAbstractResponseParser(stockTracker, keywordSearchHelper);//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        nittyGrittyResponseParser.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://www.nittygrittystore.com/men/footwear/deerupt-runner-cl_-white"), eq("Deerupt Runner"), eq("NittyGritty"), eq(null), eq(null), eq(null));

    }

    @Test
    public void testOOS(){
        when(stockTracker.notifyForObject("/men/footwear/deerupt-runner-cl_-white", false)).thenReturn(false);

        nittyGrittyResponseParser.parse(basicHttpResponse, attachmentCreater, false);
  //      Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }
}
