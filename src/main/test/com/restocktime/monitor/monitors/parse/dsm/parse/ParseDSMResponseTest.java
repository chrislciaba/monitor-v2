package com.restocktime.monitor.monitors.parse.dsm.parse;

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

public class ParseDSMResponseTest {
    private ParseDSMAbstractResponse parseDSMResponse;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "        \n" +
                "<div class=\"grid-view-item product-price--sold-out grid-view-item--sold-out\">\n" +
                "  <a class=\"grid-view-item__link\" href=\"/collections/nikelab-dsm-gnz/products/nikelab-react-element-87-undercover-bq2718-300\">\n" +
                "    \n" +
                "    <img class=\"grid-view-item__image\" src=\"//cdn.shopify.com/s/files/1/1994/0683/products/BQ2718-300_001_345x345@2x.jpg?v=1536540262\" alt=\"NikeLab  REACT ELEMENT 87 / UNDERCOVER (BQ2718-300)\">\n" +
                "    \n" +
                "    <div class=\"h4 grid-view-item__title\">NikeLab  REACT ELEMENT 87 / UNDERCOVER (BQ2718-300)</div>\n" +
                "    \n" +
                "    <div class=\"grid-view-item__meta\">\n" +
                "      <!-- snippet/product-price.liquid -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "  \n" +
                "    <span class=\"visually-hidden\">Regular price</span>\n" +
                "    <span class=\"product-price__price\">¥19,440</span>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "\n" +
                "  <span class=\"product-price__sold-out\">Sold out</span>\n" +
                "\n" +
                "\n" +
                "    </div>\n" +
                "  </a>\n" +
                "</div>\n" +
                "\n" +
                "      </div>\n" +
                "    \n" +
                "      <div class=\"grid__item grid__item--1521796576917 small--one-half medium-up--one-third\">\n" +
                "        \n" +
                "<div class=\"grid-view-item product-price--sold-out grid-view-item--sold-out\">\n" +
                "  <a class=\"grid-view-item__link\" href=\"/collections/nikelab-dsm-gnz/products/nikelab-react-element-87-undercover-bq2718-400\">\n" +
                "    \n" +
                "    <img class=\"grid-view-item__image\" src=\"//cdn.shopify.com/s/files/1/1994/0683/products/BQ2718-400_001_345x345@2x.jpg?v=1536540335\" alt=\"NikeLab  REACT ELEMENT 87 / UNDERCOVER (BQ2718-400)\">\n" +
                "    \n" +
                "    <div class=\"h4 grid-view-item__title\">NikeLab  REACT ELEMENT 87 / UNDERCOVER (BQ2718-400)</div>\n" +
                "    \n" +
                "    <div class=\"grid-view-item__meta\">\n" +
                "      <!-- snippet/product-price.liquid -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "  \n" +
                "    <span class=\"visually-hidden\">Regular price</span>\n" +
                "    <span class=\"product-price__price\">¥19,440</span>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "\n" +
                "  <span class=\"product-price__sold-out\">Sold out</span>\n" +
                "\n" +
                "\n" +
                "    </div>\n" +
                "  </a>\n" +
                "</div>";
        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://url.com/collections/nikelab-dsm-gnz/products/nikelab-react-element-87-undercover-bq2718-400", false)).thenReturn(true);


      //  parseDSMResponse = new ParseDSMAbstractResponse(stockTracker, "https://url.com", "US");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        parseDSMResponse.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://url.com/collections/nikelab-dsm-gnz/products/nikelab-react-element-87-undercover-bq2718-400"), eq("NikeLab  REACT ELEMENT 87 / UNDERCOVER (BQ2718-300)"), eq("DSM E-Flash US"), any(), any());

    }

    @Test
    public void testOOS(){
        String testInput = "<body class=\"template-password\">\n" +
                "  <div class=\"password-page text-center\">\n" +
                "    \n" +
                "\n" +
                "    <div class=\"password-main text-center\" role=\"main\">\n" +
                "      <div class=\"password-main__inner\">\n" +
                "        \n" +
                "\n" +
                "\n" +
                "\n" +
                "<div id=\"shopify-section-password-header\" class=\"shopify-section\">\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<div>\n" +
                "    \n" +
                "        <img src=\"//cdn.shopify.com/s/files/1/1994/0603/t/1/assets/dsmny-flash-hold.png?7211258721169351147\" width=\"800\" />\n" +
                "  \t\t\n" +
                "    \n" +
                "</div>\n" +
                "\n" +
                "<div id=\"shopify-section-password-content\" class=\"shopify-section\">\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "</div>\n" +
                "<div id=\"shopify-section-password-footer\" class=\"shopify-section\">\n" +
                "\n" +
                "<div class=\"password-powered-by\">\n" +
                "  \n" +
                "\n" +
                "  \n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                " \n" +
                "</body>";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        parseDSMResponse.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(false);
        parseDSMResponse.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }

}