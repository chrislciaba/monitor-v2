package com.restocktime.monitor.monitors.parse.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.patta.PattaAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class PattaResponseParserTest {
    private PattaAbstractResponseParser pattaResponseParser;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "<li class=\"item product product-item\">                <div class=\"product-item-info\" data-container=\"product-grid\">\n" +
                "                    <ul class=\"product-labels\"></ul>\n" +
                "                    <a href=\"https://www.patta.nl/catalog/product/view/id/42133/s/nike-air-max-1-dark-grey-fierce-purple-black/category/322/\" class=\"product photo product-item-photo\" tabindex=\"-1\">\n" +
                "\n" +
                "                    <div class=\"product-image-desktop\"><img src=\"https://www.patta.nl/media/catalog/product/cache/3db74c8b8734ca3bf5ae5b4fc9612a66/n/i/nike_air_max_1_dark_grey_fierce_purple_black_01.jpg\" alt=\"Preview image\" onmouseover=\"this.src='https://www.patta.nl/media/catalog/product/cache/3db74c8b8734ca3bf5ae5b4fc9612a66/n/i/nike_air_max_1_dark_grey_fierce_purple_black_0.jpg'\" onmouseleave=\"this.src='https://www.patta.nl/media/catalog/product/cache/3db74c8b8734ca3bf5ae5b4fc9612a66/n/i/nike_air_max_1_dark_grey_fierce_purple_black_01.jpg'\"></div><div class=\"product-image-mobile\"><img src=\"https://www.patta.nl/media/catalog/product/cache/3db74c8b8734ca3bf5ae5b4fc9612a66/n/i/nike_air_max_1_dark_grey_fierce_purple_black_01.jpg\" alt=\"Preview image\"></div>                    </a>\n" +
                "                    <div class=\"product details product-item-details\">\n" +
                "                                                <strong class=\"product name product-item-name\">\n" +
                "                            <a class=\"product-item-link\"\n" +
                "                               href=\"https://www.patta.nl/catalog/product/view/id/42133/s/nike-air-max-1-dark-grey-fierce-purple-black/category/322/\">\n" +
                "                                Nike Air Max 1 (Dark Grey/Fierce Purple/Black)                            </a>\n" +
                "                        </strong>\n" +
                "                                                <div class=\"price-box price-final_price\" data-role=\"priceBox\" data-product-id=\"42133\" data-price-box=\"product-id-42133\">\n" +
                "\n" +
                "\n" +
                "<span class=\"normal-price\">\n" +
                "    \n" +
                "\n" +
                "<span class=\"price-container price-final_price tax weee\"\n" +
                "        >\n" +
                "        <span  id=\"product-price-42133\"                data-price-amount=\"150\"\n" +
                "        data-price-type=\"finalPrice\"\n" +
                "        class=\"price-wrapper \">\n" +
                "        <span class=\"price\">€150.00</span>    </span>\n" +
                "        </span>\n" +
                "</span>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</div>                        \n" +
                "\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                                            </li>";
        when(keywordSearchHelper.search("Nike Air Max 1 (Dark Grey/Fierce Purple/Black)")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://www.patta.nl/catalog/product/view/id/42133/s/nike-air-max-1-dark-grey-fierce-purple-black/category/322/", false)).thenReturn(true);


       // pattaResponseParser = new PattaAbstractResponseParser(stockTracker, keywordSearchHelper);//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        pattaResponseParser.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://www.patta.nl/catalog/product/view/id/42133/s/nike-air-max-1-dark-grey-fierce-purple-black/category/322/"), eq("Nike Air Max 1 (Dark Grey/Fierce Purple/Black)"), eq("Patta"), eq(null), eq(null), eq(null));

    }

    @Test
    public void testOOS(){
        String testInput = "disabled=\"disabled\">Add to Bag";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        pattaResponseParser.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("https://www.patta.nl/catalog/product/view/id/42133/s/nike-air-max-1-dark-grey-fierce-purple-black/category/322/", false)).thenReturn(false);
        pattaResponseParser.parse(basicHttpResponse, attachmentCreater, false);
    //    Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }
}
