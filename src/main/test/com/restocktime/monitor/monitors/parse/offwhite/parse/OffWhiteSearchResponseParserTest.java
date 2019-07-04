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

public class OffWhiteSearchResponseParserTest {
    private OffWhiteSearchAbstractResponseParser offWhiteSearchResponseParser;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "<section class='products'>\n" +
                "<article class='product' data-json-url='/en/GB/men/products/omia090f18a482330100z.json' id='product_26499' itemscope='' itemtype='http://schema.org/Product'>\n" +
                "<header>\n" +
                "<h3>OMIA090F18A482330100</h3>\n" +
                "</header>\n" +
                "<a itemProp=\"url\" href=\"/en/GB/men/products/omia090f18a482330100z\"><span content='OFF WHITE Shoes OMIA090F18A482330100' itemProp='name' style='display:none'></span>\n" +
                "<span content='OFF WHITE' itemProp='brand' style='display:none'></span>\n" +
                "<span content='OMIA090F18A482330100' itemProp='model' style='display:none'></span>\n" +
                "<figure>\n" +
                "<img itemProp=\"image\" alt=\"OMIA090F18A482330100 image\" class=\"top\" src=\"https://cdn.off---white.com/images/163686/product_presto_bianche_1.jpg?1533289255\" />\n" +
                "<figcaption>\n" +
                "<div class='brand-name'>\n" +
                "Off-White™ x Nike Air Presto\n" +
                "</div>\n" +
                "<div class='category-and-season'>\n" +
                "<span class='category'>Shoes</span>\n" +
                "</div>\n" +
                "<div class='price' itemProp='offers' itemscope='' itemtype='http://schema.org/Offer'>\n" +
                "<span content='133.0' itemProp='price'>\n" +
                "<strong>£ 133</strong>\n" +
                "</span>\n" +
                "<span content='GBP' itemProp='priceCurrency'></span>\n" +
                "</div>\n" +
                "<div class='availability not_on_sale'>\n" +
                "Sold Out\n" +
                "</div>\n" +
                "<div class='size-box js-size-box'>\n";
        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/en/GB/men/products/omia090f18a482330100z", false)).thenReturn(true);

      //  offWhiteSearchResponseParser = new OffWhiteSearchAbstractResponseParser(stockTracker, "https://url.com");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        offWhiteSearchResponseParser.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://url.com/en/GB/men/products/omia090f18a482330100z"), eq("Off-White™ x Nike Air Presto"), eq("Off White"), eq(null), eq(null));

    }

    @Test
    public void testOOS(){
        String testInput = "div class='page-header'>\n" +
                "<div class='page-header-subtitle'>\n" +
                "<p class='page-subtitle'>\n" +
                "Search results: prestoadsfasdfsdfa.\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class='page-header-title'>\n" +
                "<a href=\"/en/GB\"><span class='fine-print-title'>\n" +
                "\"FINE PRINT\"\n" +
                "</span>\n" +
                "<span class='default'>\n" +
                "\"WEBSITE\"\n" +
                "</span>\n" +
                "</a></div>\n" +
                "<a class=\"page-header-logo\" href=\"/en/GB\">OFF-WHITE c/o VIRGIL ABLOH&#0153;<br>Defining the grey area between black<br>and white as the color Off-White&#0153;\n" +
                "</a><a class=\"page-header-sitename\" href=\"/en/GB\">Off-White&#0153;\n" +
                "</a></div>\n" +
                "<nav class='headers-menu'>\n" +
                "<ul class='static-links-catalog-products'>\n" +
                "<li>\n" +
                "<a href=\"/en/GB/search\">Books</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href=\"/en/GB/search\">Posters</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "<div>\n" +
                "<header>\n" +
                "\"Options\"\n" +
                "</header>\n" +
                "<section class='container'>\n" +
                "<a class=' ' href='/en/GB/section/new-arrivals' target=''>NEW ARRIVALS</a>\n" +
                "</section>\n" +
                "<section class='container'>\n" +
                "<a class=' ' href='/en/GB/collections/mens-ss19' target=''>COLLECTIONS</a>\n" +
                "</section>\n" +
                "<section class='container'>\n" +
                "<a class=' ' href='/en/GB/men' target=''>SHOP MEN</a>\n" +
                "<ul class=' '>\n" +
                "<li>\n" +
                "<a class=' ' href='/en/GB/men/t/seasons/fw2018' target=''>fw 2018</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</section>";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        offWhiteSearchResponseParser.parse(basicHttpResponse, attachmentCreater, false);
   //     Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("/en/GB/men/products/omia090f18a482330100z", false)).thenReturn(false);
        offWhiteSearchResponseParser.parse(basicHttpResponse, attachmentCreater, false);
   //     Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }
}
