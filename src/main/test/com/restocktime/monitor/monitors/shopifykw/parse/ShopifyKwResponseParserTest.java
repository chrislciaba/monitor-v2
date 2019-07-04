package com.restocktime.monitor.monitors.shopifykw.parse;

import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.LinkCheckStarter;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.shopify.parse.ShopifyKwAbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ShopifyKwResponseParserTest {
    private ShopifyKwAbstractResponseParser shopifyKwResponseParse;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;
    private Notifications notifications;
    private LinkCheckStarter linkCheckStarter;

    private HttpRequestHelper httpRequestHelper;

    @Before
    public void setup(){
        linkCheckStarter = mock(LinkCheckStarter.class);
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);
        notifications = mock(Notifications.class);
        httpRequestHelper = mock(HttpRequestHelper.class);

        String testInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\">\n" +
                "  <url>\n" +
                "    <loc>https://kith.com/</loc>\n" +
                "    <changefreq>daily</changefreq>\n" +
                "  </url>\n" +
                "  <url>\n" +
                "    <loc>https://kith.com/products/023-2wsc1kx-wav-wmns</loc>\n" +
                "    <lastmod>2018-09-14T16:06:56-04:00</lastmod>\n" +
                "    <changefreq>daily</changefreq>\n" +
                "    <image:image>\n" +
                "      <image:loc>https://cdn.shopify.com/s/files/1/0094/2252/products/023-2wsc1kx-WAV-2.jpg?v=1536858294</image:loc>\n" +
                "      <image:title>Kith x RE/DONE Boxy Tee - Blue Wave</image:title>\n" +
                "    </image:image>\n" +
                "  </url>\n" +
                "  <url>\n" +
                "    <loc>https://kith.com/products/023-2wsc1kx-wmns</loc>\n" +
                "    <lastmod>2018-09-14T16:37:11-04:00</lastmod>\n" +
                "    <changefreq>daily</changefreq>\n" +
                "    <image:image>\n" +
                "      <image:loc>https://cdn.shopify.com/s/files/1/0094/2252/products/023-2WSC1KX-3.jpg?v=1536858321</image:loc>\n" +
                "      <image:title>Kith x RE/DONE Boxy Tee - Burgundy Burst</image:title>\n" +
                "    </image:image>\n" +
                "  </url>";
        when(keywordSearchHelper.search("Kith x RE/DONE Boxy Tee - Blue Wave")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://kith.com/products/023-2wsc1kx-wav-wmns", false)).thenReturn(true);
        when(stockTracker.notifyForObject("https://kith.com/products/023-2wsc1kx-wav-wmns", true)).thenReturn(false);


       // shopifyKwResponseParse = new ShopifyKwAbstractResponseParser(stockTracker, keywordSearchHelper, true, "TEST", notifications, linkCheckStarter, true);//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStockAndKw(){
        shopifyKwResponseParse.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://kith.com/products/023-2wsc1kx-wav-wmns"), eq("Kith x RE/DONE Boxy Tee - Blue Wave"), eq("TEST"), any(), any(), eq("https://cdn.shopify.com/s/files/1/0094/2252/products/023-2wsc1kx-WAV-2.jpg?v=1536858294"));
    //    Mockito.verify(linkCheckStarter, Mockito.times(1)).generateLinkCheckStarters(any(), eq("TEST"), eq("https://kith.com"), any(KeywordSearchHelper.class), any(Notifications.class), any(AttachmentCreater.class), any(HttpRequestHelper.class), eq(true));
    }

    @Test
    public void testInStockAndNoKw(){
        when(keywordSearchHelper.search("Kith x RE/DONE Boxy Tee - Blue Wave")).thenReturn(false);

        shopifyKwResponseParse.parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verifyZeroInteractions(attachmentCreater);
        //Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://kith.com/products/023-2wsc1kx-wav-wmns"), eq("Kith x RE/DONE Boxy Tee - Blue Wave"), eq("TEST"), any(), any(), eq("https://cdn.shopify.com/s/files/1/0094/2252/products/023-2wsc1kx-WAV-2.jpg?v=1536858294"));
    //    Mockito.verify(linkCheckStarter, Mockito.times(1)).generateLinkCheckStarters(any(), eq("TEST"), eq("https://kith.com"), any(KeywordSearchHelper.class), any(Notifications.class), any(AttachmentCreater.class), any(HttpRequestHelper.class), eq(false));


    }

    @Test
    public void testNothingNew(){
        when(stockTracker.notifyForObject("https://kith.com/products/023-2wsc1kx-wav-wmns", false)).thenReturn(false);

        shopifyKwResponseParse.parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verifyZeroInteractions(attachmentCreater);
        Mockito.verifyZeroInteractions(linkCheckStarter);
        //Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testFirst(){
        shopifyKwResponseParse.parse(basicHttpResponse, attachmentCreater, true);
        Mockito.verifyZeroInteractions(attachmentCreater);
        Mockito.verifyZeroInteractions(linkCheckStarter);
        //Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }
}

