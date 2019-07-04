package com.restocktime.monitor.monitors.parse.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.parse.acronym.AcronymParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class AcronymParseResponseTest {
    private AcronymParser acronymParseResponse;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private  BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "  <a class=\"tile \" href=\"/products/J68-S_SS18\">\n" +
                "\n" +
                "\n" +
                "    <div class=\"name\">J68-S</div>\n" +
                "    <img src=\"/content/images/product/275/6357/fbda4c8df7_tile.jpg\" alt=\"Fbda4c8df7 tile\" />\n" +
                "\n" +
                "    <div class=\"product-info\">\n" +
                "      <div class=\"\">\n" +
                "        <img src=\"/content/images/product/275/6357/fbda4c8df7_tile.jpg\" alt=\"Fbda4c8df7 tile\" />\n" +
                "        <h2>J68-S</h2>\n" +
                "      </div>\n" +
                "      <div class=\"txt\">\n" +
                "        <div>HD Cotton Rider Jacket</div>\n" +
                "          <span class=\"selected\">SOLD OUT</span>\n" +
                "        <span>Lightshell</span>\n" +
                "          <span>Gen. 1</span>\n" +
                "          <span>Black ??? RAF Green ??? White</span>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    \n" +
                "</a>\n" +
                "\n";

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        s = new SlackObj[0];
        d = new String[0];
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);


       // acronymParseResponse = new AcronymParser(stockTracker, keywordSearchHelper, "https://url.com");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testNotifyTrue(){

        acronymParseResponse.parse(basicHttpResponse, attachmentCreater,false);
      //  Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages("https://url.com/products/J68-S_SS18", "HD Cotton Rider Jacket", "Acronym", null, null, "https://url.com/content/images/product/275/6357/fbda4c8df7_tile.jpg");

    }

    @Test
    public void testNotifyNoKw(){
        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(false);

        acronymParseResponse.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testNotifyAlreadyFound(){
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(false);

        acronymParseResponse.parse(basicHttpResponse, attachmentCreater, false);
    //    Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }
}
