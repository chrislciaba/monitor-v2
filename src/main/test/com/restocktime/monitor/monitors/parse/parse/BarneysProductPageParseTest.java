package com.restocktime.monitor.monitors.parse.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.parse.barneys.BarneysParseProductAbstractResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class BarneysProductPageParseTest {
    private BarneysParseProductAbstractResponse barneysParseProductResponse;
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

        String testInput = "  \n" +
                "\t<title>\n" +
                "\t\tVEJA V-10 Leather & Suede Sneakers | BarneysTest New York\n" +
                "\t</title>" +
                "\n<input id=\"skuId\" name=\"/atg/commerce/order/purchase/CartModifierFormHandler.items[0].catalogRefId\" value=\"\" type=\"hidden\"/><input name=\"_D:/atg/commerce/order/purchase/CartModifierFormHandler.items[0].catalogRefId\" value=\" \" type=\"hidden\"/><input name=\"/atg/commerce/order/purchase/CartModifierFormHandler.items[0].productId\" value=\"505875099\" type=\"hidden\"/><input name=\"_D:/atg/commerce/order/purchase/CartModifierFormHandler.items[0].productId\" value=\" \" type=\"hidden\"/>\n" +
                "        <span class=\"atg_store_basicButton add_to_cart_link \">\n" +
                "           <input id=\"atg_behavior_addItemToCart\" data-atp=\"\" name=\"/atg/commerce/order/purchase/CartModifierFormHandler.addItemToOrder\" value=\"add to bag\" class=\"atg_behavior_addItemToCart pdp-bny-Button productLevelAddToCart  \" data-productid=\"505875099\" type=\"submit\"/><input name=\"_D:/atg/commerce/order/purchase/CartModifierFormHandler.addItemToOrder\" value=\" \" type=\"hidden\"/>\n" +
                "        </span>\n" +
                "      \n" +
                "      </div>\n" +
                "\n" +
                "      <div style=\"display:none\"><input name=\"_DARGS\" value=\"/browse/gadgets/ajaxPickerContents.jsp.addToCart\" type=\"hidden\"/> </div></form>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                "  \n" +
                "    <span class=\"gwp-container hideForPDP\">\n" +
                "      \n" +
                "    </span>\n" +
                "  \n" +
                "                       </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "               \n" +
                "                </div>\n" +
                "              </div>\n" +
                "                  \n" +
                "  </div>";

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("https://url.com", false)).thenReturn(true);


        //barneysParseProductResponse = new BarneysParseProductAbstractResponse(stockTracker, "https://url.com", "");//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStockResponse(){
        barneysParseProductResponse.parse(basicHttpResponse, attachmentCreater, false);
       // Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages("https://url.com", "VEJA V-10 LEATHER & SUEDE SNEAKERS | BARNEYSTEST NEW YORK", "Barneys", null, null);

    }

    @Test
    public void testOOSStockResponse(){
        String testInput = "<div class=\"visible-xs\">\n" +
                "\t<span id=\"apple-pay-error\" class=\"apple-pay-error\"></span>\n" +
                "\t\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "<span id=\"add_item_cart_fp\" data-fpproductid=\"505700567\" data-fpselectedsize=\"\" data-fpcolor=\"GRAY\" data-customerid=\"1632011518\" data-fpquantity=\"\" data-fpsaleprice=\"\" data-fpskuid=\"\"></span>\n" +
                "\n" +
                "\n" +
                "        <span class=\"atg_store_basicButton add_to_cart_link prd-out-of-stock\">\n" +
                "           <input id=\"atg_behavior_addItemToCart\" name=\"/atg/commerce/order/purchase/CartModifierFormHandler.addItemToOrder\" value=\"out of stock\" class=\"atg_behavior_addItemToCart pdp-bny-Button productLevelAddToCart  out-of-stock-btn\" type=\"submit\" disabled=\"disabled\"/><input name=\"_D:/atg/commerce/order/purchase/CartModifierFormHandler.addItemToOrder\" value=\" \" type=\"hidden\"/>\n" +
                "        </span>\n" +
                "      \n" +
                "      </div>\n" +
                "\n" +
                "      <div style=\"display:none\"><input name=\"_DARGS\" value=\"/browse/gadgets/ajaxPickerContents.jsp.addToCart\" type=\"hidden\"/> </div></form>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                "  \n" +
                "    <span class=\"gwp-container hideForPDP\">\n" +
                "      \n" +
                "    </span>\n" +
                "  \n" +
                "                       </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "               \n" +
                "                </div>\n" +
                "              </div>\n" +
                "                  \n" +
                "  </div>";

        when(basicHttpResponse.getBody()).thenReturn(testInput);
        barneysParseProductResponse.parse(basicHttpResponse, attachmentCreater, false);
       // Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }
}
