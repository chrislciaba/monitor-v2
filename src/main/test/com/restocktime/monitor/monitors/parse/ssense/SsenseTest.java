package com.restocktime.monitor.monitors.parse.ssense;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.ssense.Ssense;
import com.restocktime.monitor.monitors.parse.ssense.parse.PageResponseParser;
import com.restocktime.monitor.monitors.parse.ssense.parse.SearchResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.powermock.api.mockito.PowerMockito;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class SsenseTest {
    private Ssense acronym;
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private BasicRequestClient basicRequestClient;
    private Notifications notifications;



    @Before
    public void setup(){
        basicRequestClient = mock(BasicRequestClient.class);

        httpRequestHelper = mock(HttpRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
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
                "          <span>Black • RAF Green • White</span>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    \n" +
                "</a>\n" +
                "\n";

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        BasicHttpResponse basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(httpRequestHelper.performGet(basicRequestClient, "https://url.com")).thenReturn(basicHttpResponse);
        s = new SlackObj[0];
        d = new String[0];
        PowerMockito.mockStatic(Notifications.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);
        PageResponseParser pageResponseParser = mock(PageResponseParser.class);
        SearchResponseParser searchResponseParser = mock(SearchResponseParser.class);
      //  acronym = new Ssense("https://url.com", 0, "US",notifications, attachmentCreater, httpRequestHelper, pageResponseParser,searchResponseParser);
    }
}
