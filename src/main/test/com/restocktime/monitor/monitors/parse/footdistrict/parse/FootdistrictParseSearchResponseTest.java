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

public class FootdistrictParseSearchResponseTest {
    private FootdistrictParseSearchAbstractResponse footdistrictParseSearchResponse;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "\n" +
                "    \n" +
                "                            <ul class=\"products-grid\">\n" +
                "                    <li class=\"item first\">\n" +
                "                <a href=\"https://footdistrict.com/en/quickview/index/view?pid=125242\" data-fancybox-type=\"iframe\" class=\"mp_quickview_icon mobilehidden\" id=\"mp_quickview_125242\"><span style=\"text-transform:uppercase;\">Quick View</br><i class=\"fa fa-eye fa-2x\"></i></span></a>\n" +
                "                <a href=\"https://footdistrict.com/en/nike-wmns-air-more-money-lx-aj1312-003.html\" title=\"Nike WMNS Air More Money LX  \" class=\"product-image\"><img src=\"https://footdistrict.com/en/media/extendware/ewimageopt/media/inline/ad/f/a1def73cf407f762a3ddb5edf870029d/nike-wmns-air-more-money-lx-aj1312-003-20.jpg\" width=\"100%\" alt=\"nike-wmns-air-more-money-lx-aj1312-003-20\" /></a>\n" +
                "\n" +
                "                <div id=\"125242\" class=\"mp_sticky not_sticky\" onclick=\"book_mark_item(this,'125242')\"></div>\n" +
                "                <h2 class=\"product-name\"><a href=\"https://footdistrict.com/en/nike-wmns-air-more-money-lx-aj1312-003.html\" title=\"Nike WMNS Air More Money LX  \">Nike WMNS Air More Money LX  </a></h2>\n" +
                "                    <span ><div class=\"product-attribute\">Ref: AJ1312-003</div></span>\n" +
                "\n" +
                "\n" +
                "                \t\t\n" +
                "                <div class=\"price\" id=\"amlabel-product-price-125242\" style=\"display:none\"></div>  \n" +
                "\n" +
                "                \n" +
                "    <div class=\"price-box\">\n" +
                "                                                            <span class=\"regular-price\" id=\"product-price-125242\">\n" +
                "                                            <span class=\"price\">€170</span>                                    </span>\n" +
                "                        \n" +
                "        </div>\n" +
                "\n" +
                "\n" +
                "            </li>";
        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://footdistrict.com/en/nike-wmns-air-more-money-lx-aj1312-003.html", false)).thenReturn(true);


      //  footdistrictParseSearchResponse = new FootdistrictParseSearchAbstractResponse(stockTracker);//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        footdistrictParseSearchResponse.parse(basicHttpResponse, attachmentCreater, false);
       // Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://footdistrict.com/en/nike-wmns-air-more-money-lx-aj1312-003.html"), eq("Nike WMNS Air More Money LX  "), eq("FootDistrict"), any(), any());

    }

    @Test
    public void testOOS(){
        String testInput = "<ul>\n" +
                "                    <li class=\"home\">\n" +
                "                            <a href=\"https://footdistrict.com/en/\" title=\"Go to Home Page\">Home</a>\n" +
                "                                        <span> > </span>\n" +
                "                        </li>\n" +
                "                    <li class=\"search\">\n" +
                "                            Search results for: 'AA3131-0000000000'                                    </li>\n" +
                "            </ul>";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        footdistrictParseSearchResponse.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(false);
        footdistrictParseSearchResponse.parse(basicHttpResponse, attachmentCreater, false);
   //     Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }
}
