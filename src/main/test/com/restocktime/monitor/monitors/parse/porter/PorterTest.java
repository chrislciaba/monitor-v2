package com.restocktime.monitor.monitors.parse.porter;


import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.ingest.porter.Porter;
import com.restocktime.monitor.monitors.parse.porter.parse.ApiAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.porter.parse.AtcResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Notifications.class)
public class PorterTest {
    private Porter porter;
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private Notifications notifications;
    private ApiAbstractResponseParser apiResponseParser;
    private AtcResponseParser atcResponseParser;
    private BasicHttpResponse basicHttpResponse;
    private BasicRequestClient basicRequestClient;


    @Before
    public void setup(){
        basicRequestClient = mock(BasicRequestClient.class);

        httpRequestHelper = mock(HttpRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);
        notifications = mock(Notifications.class);
        apiResponseParser = mock(ApiAbstractResponseParser.class);
        atcResponseParser = mock(AtcResponseParser.class);
        basicHttpResponse = mock(BasicHttpResponse.class);

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("/products/J68-S_SS18", false)).thenReturn(true);

      // porter = new Porter("https://url.com", "US", 0, notifications, attachmentCreater, httpRequestHelper, apiResponseParser, atcResponseParser);
    }

    @Test
    public void testNotifySearchTrue(){
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com"))).thenReturn(basicHttpResponse);
        porter.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com"));
        Mockito.verify(apiResponseParser, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }
    /*private Porter porter;
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private KeywordSearchHelper keywordSearchHelper;
    private SlackObj[] s;
    private String[] d;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        httpRequestHelper = mock(HttpRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String apiResp = "{\"longDescription\":\"<a href=\\\"http://www.mrporter.com/mens/Designers/Churchs\\\">Church's</a> English-made footwear is a hallmark of quality and good taste. These black 'Dubai' Oxfords are crafted from hand-selected leather and finished with a glossy polished binder technique. The buffing waxes are applied gradually to enhance the depth and natural patina of each individual hide, whilst the Goodyear®-welted soles ensure durability. The result is a beautiful, custom-grade shoe that has a unique appearance. Team this dapper pair with sharp suiting. Shown here with a [Kingsman scarf id628462], [Burberry London trench coat id536707], [sweater id636500] and [shirt id587058] and [Canali trousers id567328].\",\"name\":\"Dubai Polished-Leather Oxford Shoes\",\"sizeFit\":\"- Fits true to size. Take your normal size\\r\\n- Fit F = US D (medium width)\",\"visible\":true,\"price\":{\"currency\":\"USD\",\"divisor\":100,\"amount\":65000},\"onSale\":false,\"brand\":{\"id\":543,\"name\":\"Church's\",\"urlKey\":\"Churchs\"},\"analyticsKey\":\"Dubai Polished-Leather Oxford Shoes\",\"centralSizeScheme\":\"M Shoes - UK half size\",\"id\":629145,\"skus\":[{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":30.0}],\"standardSizeId\":\"00015_40_Shoes\",\"displaySize\":\"UK6\",\"moreComingSoon\":false,\"id\":\"629145-1082\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":30.5}],\"standardSizeId\":\"00017_41_Shoes\",\"displaySize\":\"UK7\",\"moreComingSoon\":false,\"id\":\"629145-1084\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":31.0}],\"standardSizeId\":\"00018_41.5_Shoes\",\"displaySize\":\"UK7.5\",\"moreComingSoon\":false,\"id\":\"629145-1085\"},{\"stockLevel\":\"In_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":31.5}],\"standardSizeId\":\"00019_42_Shoes\",\"displaySize\":\"UK8\",\"moreComingSoon\":false,\"id\":\"629145-1086\"},{\"stockLevel\":\"In_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":32.0}],\"standardSizeId\":\"00020_42.5_Shoes\",\"displaySize\":\"UK8.5\",\"moreComingSoon\":false,\"id\":\"629145-1087\"},{\"stockLevel\":\"In_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":32.5}],\"standardSizeId\":\"00021_43_Shoes\",\"displaySize\":\"UK9\",\"moreComingSoon\":false,\"id\":\"629145-1088\"},{\"stockLevel\":\"Out_of_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":33.0}],\"standardSizeId\":\"00022_43.5_Shoes\",\"displaySize\":\"UK9.5\",\"moreComingSoon\":false,\"id\":\"629145-1089\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":33.5}],\"standardSizeId\":\"00023_44_Shoes\",\"displaySize\":\"UK10\",\"moreComingSoon\":false,\"id\":\"629145-1090\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":34.0}],\"standardSizeId\":\"00025_45_Shoes\",\"displaySize\":\"UK11\",\"moreComingSoon\":true,\"id\":\"629145-1092\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":34.5}],\"standardSizeId\":\"00027_46_Shoes\",\"displaySize\":\"UK12\",\"moreComingSoon\":false,\"id\":\"629145-1094\"}],\"designerShippingRestriction\":false,\"categories\":[{\"id\":3573,\"name\":\"Shoes\",\"urlKey\":\"Shoes\",\"children\":[{\"id\":9762,\"name\":\"Oxford shoes\",\"urlKey\":\"Oxford_shoes\",\"children\":[{\"id\":9792,\"name\":\"Oxfords\",\"urlKey\":\"Oxfords\"}]}]}],\"keywords\":[\"DUBAI\",\"churchs\",\"churches\",\"oxfords\",\"investments\",\"work\",\"office\",\"formal\",\"events\",\"occasions\"],\"colourIds\":[2],\"nonReturnable\":false,\"colourVariations\":[1078550],\"images\":{\"shots\":[\"fr\",\"e1\",\"bk\",\"ou\",\"in\",\"cu\",\"e2\",\"e3\"],\"sizes\":[\"l\",\"m2\",\"m3\",\"s\",\"xl\",\"xs\",\"xxl\"],\"mediaType\":\"image/jpeg\",\"urlTemplate\":\"{{scheme}}//cache.mrporter.com/images/products/629145/629145_mrp_{{shot}}_{{size}}.jpg\"},\"editorsComments\":\"- Black polished-leather\\r\\n- Leather linings, reinforced heels, Goodyear®-welted soles\\r\\n- Lace-up\\r\\n- Comes with dust bags\\r\\n- Made in England\",\"badges\":[\"PP_Essentials\",\"In_Stock\"]}";



        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(apiResp);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(httpRequestHelper.performGet(eq(closeableHttpClient), eq(requestConfig), any(), eq("https://url.com/MRP/detail"), any())).thenReturn(basicHttpResponse);
        s = new SlackObj[0];
        d = new String[0];
        PowerMockito.mockStatic(Notifications.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("629145-1082", false)).thenReturn(true);

        porter = new Porter("https://url.com/MRP/detail", "test-sku", "MR PORTER TEST", "US", 0, s, d, attachmentCreater, httpRequestHelper, stockTracker);
    }

    @Test
    public void testRun(){
        porter.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://www.mrporter.com/en-us/mens/product/test-sku"), eq("Dubai Polished-Leather Oxford Shoes"), eq("Porter US"), any(), any());
    }

    @Test
    public void testOutOfStock(){
        String apiResp = "{\"longDescription\":\"<a href=\\\"http://www.mrporter.com/mens/Designers/Churchs\\\">Church's</a> English-made footwear is a hallmark of quality and good taste. These black 'Dubai' Oxfords are crafted from hand-selected leather and finished with a glossy polished binder technique. The buffing waxes are applied gradually to enhance the depth and natural patina of each individual hide, whilst the Goodyear®-welted soles ensure durability. The result is a beautiful, custom-grade shoe that has a unique appearance. Team this dapper pair with sharp suiting. Shown here with a [Kingsman scarf id628462], [Burberry London trench coat id536707], [sweater id636500] and [shirt id587058] and [Canali trousers id567328].\",\"name\":\"Dubai Polished-Leather Oxford Shoes\",\"sizeFit\":\"- Fits true to size. Take your normal size\\r\\n- Fit F = US D (medium width)\",\"visible\":true,\"price\":{\"currency\":\"USD\",\"divisor\":100,\"amount\":65000},\"onSale\":false,\"brand\":{\"id\":543,\"name\":\"Church's\",\"urlKey\":\"Churchs\"},\"analyticsKey\":\"Dubai Polished-Leather Oxford Shoes\",\"centralSizeScheme\":\"M Shoes - UK half size\",\"id\":629145,\"skus\":[{\"stockLevel\":\"Out_of_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":30.0}],\"standardSizeId\":\"00015_40_Shoes\",\"displaySize\":\"UK6\",\"moreComingSoon\":false,\"id\":\"629145-1082\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":30.5}],\"standardSizeId\":\"00017_41_Shoes\",\"displaySize\":\"UK7\",\"moreComingSoon\":false,\"id\":\"629145-1084\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":31.0}],\"standardSizeId\":\"00018_41.5_Shoes\",\"displaySize\":\"UK7.5\",\"moreComingSoon\":false,\"id\":\"629145-1085\"},{\"stockLevel\":\"In_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":31.5}],\"standardSizeId\":\"00019_42_Shoes\",\"displaySize\":\"UK8\",\"moreComingSoon\":false,\"id\":\"629145-1086\"},{\"stockLevel\":\"In_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":32.0}],\"standardSizeId\":\"00020_42.5_Shoes\",\"displaySize\":\"UK8.5\",\"moreComingSoon\":false,\"id\":\"629145-1087\"},{\"stockLevel\":\"In_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":32.5}],\"standardSizeId\":\"00021_43_Shoes\",\"displaySize\":\"UK9\",\"moreComingSoon\":false,\"id\":\"629145-1088\"},{\"stockLevel\":\"Out_of_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":33.0}],\"standardSizeId\":\"00022_43.5_Shoes\",\"displaySize\":\"UK9.5\",\"moreComingSoon\":false,\"id\":\"629145-1089\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":33.5}],\"standardSizeId\":\"00023_44_Shoes\",\"displaySize\":\"UK10\",\"moreComingSoon\":false,\"id\":\"629145-1090\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":34.0}],\"standardSizeId\":\"00025_45_Shoes\",\"displaySize\":\"UK11\",\"moreComingSoon\":true,\"id\":\"629145-1092\"},{\"stockLevel\":\"Low_Stock\",\"measurements\":[{\"id\":98,\"name\":\"Heel Height\",\"value\":2.5},{\"id\":110,\"name\":\"Shoe Length\",\"value\":34.5}],\"standardSizeId\":\"00027_46_Shoes\",\"displaySize\":\"UK12\",\"moreComingSoon\":false,\"id\":\"629145-1094\"}],\"designerShippingRestriction\":false,\"categories\":[{\"id\":3573,\"name\":\"Shoes\",\"urlKey\":\"Shoes\",\"children\":[{\"id\":9762,\"name\":\"Oxford shoes\",\"urlKey\":\"Oxford_shoes\",\"children\":[{\"id\":9792,\"name\":\"Oxfords\",\"urlKey\":\"Oxfords\"}]}]}],\"keywords\":[\"DUBAI\",\"churchs\",\"churches\",\"oxfords\",\"investments\",\"work\",\"office\",\"formal\",\"events\",\"occasions\"],\"colourIds\":[2],\"nonReturnable\":false,\"colourVariations\":[1078550],\"images\":{\"shots\":[\"fr\",\"e1\",\"bk\",\"ou\",\"in\",\"cu\",\"e2\",\"e3\"],\"sizes\":[\"l\",\"m2\",\"m3\",\"s\",\"xl\",\"xs\",\"xxl\"],\"mediaType\":\"image/jpeg\",\"urlTemplate\":\"{{scheme}}//cache.mrporter.com/images/products/629145/629145_mrp_{{shot}}_{{size}}.jpg\"},\"editorsComments\":\"- Black polished-leather\\r\\n- Leather linings, reinforced heels, Goodyear®-welted soles\\r\\n- Lace-up\\r\\n- Comes with dust bags\\r\\n- Made in England\",\"badges\":[\"PP_Essentials\",\"In_Stock\"]}";
        when(basicHttpResponse.getBody()).thenReturn(apiResp);

        porter.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testRunTimeout(){
        when(stockTracker.notifyForObject("629145-1082", false)).thenReturn(false);

        porter.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testNotRunYet() {
        porter.run(requestConfig, closeableHttpClient, true, false);
        Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }*/

}
