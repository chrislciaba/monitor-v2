package com.restocktime.monitor.monitors.nikescratch.parse;

import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.snkrs.parse.HuntResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class NikeScratchResponseParserTest {
    private HuntResponseParser nikeScratchResponseParser;
    private AttachmentCreater attachmentCreater;
    private KeywordSearchHelper keywordSearchHelper;
    private StockTracker stockTracker;
    private BasicHttpResponse basicHttpResponse;

    private String PASS = "{\"objects\":[{\"scanned\":null,\"success\":false,\"redeemed\":null,\"resultData\":null,\"completed\":null,\"expires\":\"2018-09-24T00:00:00.00Z\",\"discovered\":null,\"valid\":\"2018-09-21T15:18:00.00Z\",\"date\":\"2018-09-23T17:34:36.00Z\",\"type\":\"26f5ddb3-c597-43da-9680-8dade5af6201\",\"id\":\"644cd70c-babc-11e8-a98d-ce9105d17f52\",\"metadata\":\"MzZkNTEzNzI4YjJlZmJjMTQyZDJmOWViMTE3NmJkNjVhNWNhYjhiZGExN2VkMzdiZmUzMmU5NDgyNTdkMDE1ODBlYjk3NjU5NmFiMDkzOTllZmFiYzFhZDZiNDJmNDQ1YzFlMzg4N2UzZjJlNDk5ZGU5OGViZTZlYWJjZmQ1NjRiYmE0NzM2MDBjOGVkMzA0NDgwYWRlNDFhOTQwM2YwZmUwMTFlNTViMTQ3MGVhMTYxY2U4YmFjMGI2YzExYzUxODA2NzMwZTc3ZjFhMzc2YjVhMDlmMzFhZWY1NzZlNjc3YzI4MDJkZTY5MmYzM2MxMTk2ZDZiMGNlMDY0YzBhOThiNThjZDA4ZjdjYjQ4YzYzMzY1YmZiZDQwMmEwYjFiOGRkNWY5NmZhNThlMGZjMzIwNTQ3ZWM0YjBjNDMyMjExMzM3ZDIyYjRkMDkxOTE4NjY4Y2JhNzBlZGIzMGM5MmI3ODRjMWVhYTk1MWZkMDU5MDVjZjk2OWIwY2EzNjI1Nzg5M2FmN2Y2ZWI3MjYyNWJkOWI5ZTM0YTA4NGZiOTg3MGRhYWJkZjNmNGZhZjc0ODdkYzJjZmJlODA1MzVkZGY0MzVhMTRkOTgxODkwZmY3ZGRiMmY5YWFhOTJhNzUyMjgyMDMzMDM0Mjk4NzQxYTNiOTU5Yzk5ZmJhODFhMDQyNDNjNDc4ZGNjZjg2OGJmYTI4YTA0ZTUyYzAyOGI1YjBhYjgzZTA0NjMzMGQzYTUyMmY1NGI3MzMxZjFjNDk1NDIwYjYzMTU2Y2QxNDM2NGY4ZGNhMDczYmY4Y2JkOTkzMzE3ZDViNWYxNjhmNWJlYTA2NDk0ZTYzZDlmZjcyYjk5YTM3YTBmY2VhNGFiMjU4MDA3MzUwMjY4ZGZhODkxN2VjMTMxMGY3YjM4NmRlZGE2ZmJhNTQ2NGYwN2M2MTc3OTY0ZDhlODBhZWZkMDIxMTk1ZmFjNTBjMjY1NjY5MTg2Yzc0MzU0MjdiOTZkNjVkZjMyMGNiNmYzMDUwNmQ2MmY1NDc3ZjQyYTlmNjQyMDhlZjc1MjgzZWFkZDNjMDZkNTUxNGFkOTYzNjRiMWIyYWM5NmY3ZTAzODE5ZjViNDk4NTZkNTg5YWU2ZGZjNzU1Yjg1ZDMwMzUyZGNmOTU2MDQyZTUyYWJmZmZhOTRiZWU0MjI3NmE3ZGFkOGRjMTRkZjE4OGQwZjE5M2RiNTQxMzU5ODkzODVkNGUzOTdmNjBjYTllYWYzY2U1Y2JmZTJjMjBhMjNmNDA3MDg3YWZlYjNlNjhmNTdiOGI2YzM0Mjg1NGI2OTlhNjQ2YzAwZTUzMDg4NGRkNmJkYzMyNTkxNGFlNmM1NTYxYjhlN2I1OTE1MDMzZGRmMTU0ZTM1YTliNDJmOTczZmFjYTRiYWZmZTU2Mzc2YTkwOGVhMTg5NTI1YzNkNDFlMzU5ODAwMTdiMTE3NDFiNWNjOGI2NjMyZTRiYzQ1YzgxYzc1MTE3MDI3YWE4MzlhY2NiN2IyNzAzYTg5OTEzZDllZGFjZDYzY2Y1OTA2MzVmYjM0ZmQ0MTY5MzljZmRhNWIzMTI1ZjBjODczZGI1NmI0ZTE5ZTAyMjRiOTQ1MmI4NTJkNzRjODFjMjk0MjcxMGM5NzM3MTBjNzI4ZjllZTBjYjE2NTEwYmVmMTU4YWY3OTkyNDkxM2ZiM2NlM2JkODgwY2RlZTNlMDcxZjVhMDJkYmQwYmRlYzU4MDg0NWI3ZDhkZGJkNzIzYmViNjM5NjgzODU2MWZjYWUyMDVlYzFmZDM2NDY3ZTZlOTk2YjAzNDQzNmQ4OTVmYzJlMWEzOTJhYzNjNmVjM2M1YTVmZWNjMmU3NjQ5MWM0NDg5ZDZiMGFiMDZiZjg5Yjg0NGU3Mzc5YTdkY2M5Mzc3N2U4NTZkYjY0MjY5M2Y3ZTlkZTU2YThiZjQyM2NkZTFiMWYxMjZhYjEzYzY2ZDFjNmZmNGM5MGNmMmU2ZmE5NmYwNzkzYTZlM2VhOGE0OTQ0YjUxNjI0NjFjN2EyNWFmMGQ2NzY0N2NhZjJiNzFmNDZhNjUxOWIzNjc5NDE4ZWQ2MmE1MDM3MzVjM2I4NmI4NjIzMjc2NjU4YzAzZjhhZDQwMWU5OTYwM2IwNDYzOTMyMzY2OTA5Y2I1NmE1MjEwNTE3YzFiOTMyODI3NmJiMzY0NzgwM2UxYWQ0MTc1ZjVjYzRkODcyODhiZDBiMzhjMjhmNjhjMDg3NjdhN2M0ZTk2ODJjNmEzNjQwMDE4ZjM4OTA0YWExMzIyMDU0ZGQ4ZGE5Njg4Yjk2NjQwMDJiOGE0M2M4YTdkMjgxNjRlOGQ0M2FhZWZlYzRlMDNlYTE4ZGZjMzY1NzdkOTE1OTJlMmJlYzg0ZjI2NDM4ODVhNjg4MTJjNjczNTQ2NWYwMWQ1YzE5M2U5YmRhMDE1NmIyMmEzZjJjMWE3ZGViZTJlYTVhMTRkZmZmN2Q3ZTNkNjc1ZmUzM2EzNzViYjhiNDE0NzcwYTZjMTQzYTBhZWRiYWY2MjAyNzBjYjZmMTdkMmJhMWZjMDQ4MDUxODVlYjg5M2IzZWNjMmEyYTNhNDAyNjJlYWVjNmIyZDg3NThkY2U3MjQzNTc3YTc3YWIzNWY3OWExMGQ3OTUzOTBhNDBlZDMxMjc0ZmFjNDNjOTVmYWIyNzNkNWViODQ3NzNmYzZhZmVmOGUxYTY1ZDI5YWY3YWQwYTY2ZDcxMzVmMzhiN2ZlM2I4NTNjNzZmMjA3OThkMzMxOTlkNmRhYjJkOWRlMDFlNTJjNTUyZDcxZTA4ZjYxODlmYzNlZWM1ZjcxZmZhMjI5YjNiYTExOWNlMjNmYTBhNDA0YzgyZGZiOTcwY2EyMDYxZGU0NTJmZWI3NWVmYTljZTg5NWI5MjE3ZmZjZjRkMDM2ODhiODQ1NmI5ZmE0YWY5NGJjNWUyZjYwY2I1NjFlNGVmNDE0ZjZjN2MxNGZlNjgzNGI0OWNhOTA5OTczMjMyZmRmMDYyMTRiNmY1NzdmZGRkYzBkYWU5MGIyYjY4MWQxOGUwMzhmYmE5N2Q4NTMzMjgyYTk1MWRk\"},{\"scanned\":null,\"success\":false,\"redeemed\":null,\"resultData\":null,\"completed\":null,\"expires\":\"2018-09-24T00:00:00.00Z\",\"discovered\":null,\"valid\":\"2018-09-21T15:18:00.00Z\",\"date\":\"2018-09-23T17:34:36.00Z\",\"type\":\"26f5ddb3-c597-43da-9680-8dade5af6201\",\"id\":\"10e44522-babd-11e8-a98d-ce9105d17f52\",\"metadata\":\"OGI2OTdlYmQ0MjBlNTRjZjRlMmUxY2I4MWNhNDBlOGY0YThhOTU0MzliYjZkMWViYWI5OTdmMjk1MTM5YmRkMzBjYzk3OGYzZGM3OWExMGQ0OGQxZDdhYmQ1MjU5OTUxMmMxZDVhNTY2N2EzN2VjMmViNzllOGVhNzMzMzBiY2Y4NmNlNTYyNzZiMjZiMzE5ZTU4NGM4N2IxOWZmNjEwOTAwMmIyYTIxYzdkOGZjNTc2NjdjNzMxM2JjNjRkNjlhOWNhOTBiZWZkODFiOGU2MzNkMjE1NGQ0MGE0ZDk3N2ZhZjFmY2VkZGNmNWUyOWUyZDkxYzk2ODVhYjkyYWI2MmQ1MmY3YTA3ZGVjOGUyNmY3YzdhMmU1ZGVhZGYxMjlkNTA3OWRiYmFlMzVkZjJhYWYwYjA5OGYwMjFmN2RmYTQ3YjZlMTg0MzQ0MjQ5OGQ2ZmIyZTViNjY4OGRmYjhjMGFjYjRmN2VhZDNlMzAzZTJlZWVmZmE0ZWRiZDQ5YTdhMmJmOWIwMzIxNDAzMmU4MjdjN2M1NDNiNmYzOWFhYTIwMThjOTUxYmVhNWNlY2I2OGQ2MWFlZDBmNjEzNTJlMDlmYjQ3ZTM0MDI3NWY1NGNiNTMxZGRkOWVhMjk5NzJkYmNiMDNlMjU2NTkzMzQzMzkxYjllZmZlY2VlZDExMmE0ODc0NjY3YjliOTA0ODJmZTExNTg2OWU5ODRiNzUxMDc1ODMzODkwMzhkODcxNWI3ODAzYWUzODY0MjA3NjM5Njc2NjQwMGE3NjJlYjFiZWY0YmM5NTY3N2VmMmE5ZTliZThmOTMzNGJkMWY1Njc4NWZlYWIzYjM0NWFjYzU0ZWFjMzk3MmQwNDljNDRmNjhmMzE2MTA1YzBkZDQ4ODUwY2Q0MGYzYmFlN2ExNTNjZDM5MjM2ZDE3ZjhhZDc3NTViN2MxODQ1YjA2MzQ1N2JlOGRhMDUwZWM5YWJlZDZhYmFjMWFjMDkzNmMzMDlmZWY5MmY0NzI5OGExMzg3NDBiNjA0MTZiYjZlYWRkZjJlMjliZjhjYzdiMWVjMmY1MzFhZjI1NTRhODc3MDQxNjM2MDIyNDk4NTdiY2YxNjQzZGY3OTE1MGMxNTI1MjYzZDVlOGU1NjUzOTQ0YWM1YmZmZDg0NmU5ZDIxODRlMGU3M2RlYWFmMjBkZTY4ZDQ1NWZlNzQ5YmNkMmUzODE2ZWRkNDkwMDgxMmYxN2E0YTBkMzNkN2UwOGU3NWJjNDBiOGUxOWE5MjkxYWNhOGE5ODNkYzlhN2I2OTQyNjM4NmNmMDU2ZDdiOWZmN2Q4NzU3Nzc3NDI5YTdlNzc4NzcxYjFjODgxMzBkYmNlMDNhOGEyZDIwNGUzODQzMzhmMWMwYmQ4ODJmOTBjNThlZmNiYjc5MTE3NmJjODAyNzQ4NGIyYmJmMjVmYzk0MzQ2MzE4NDkyMzUyZDg2ODNkN2M2N2YyYjQzYzllYzk1ZWE2ZDM2YWViYzg4MDNlN2QwZWFhYjJlMTg3MjRlNGExNTU1ZjNjNzBmMzcwNDJiZmIwNzJkZmE3MGQxOGIyYzQzYjg4ZmJlZDYyZTg1ZDMyOTI2MjI2NTZjOGZhNzA2ZmY5MzUxNzYzZGQ5ZjJkYzc0YzRkYzI3OTU0NjViMDJiYjY4N2M5MzI1YmJkNzdjNjRkN2U2MTY2MDk5ZDNhZDQwNjk2NmY3YTc3NGZiNjJlMmQxZTA5ZmZiMGQ0OTFmZWYwNzc1ZWM3MGZmNWUzZTk1NWE0NTA0ZmVhNWIzNzljZmQwYTMxMjRkOTVjMjUyZGZhYTM1NTE3MGMyNjI0MDRmNzdlNWVlYjU4YzFhYWU5MzZiYzQxOGYzYTVkYzhkYmQ1MTQzYjcwYjhkODg5ZDNmZjYyZmY1Mjc3MDJhZjY2OGJlNjM1Y2NlMDg2ZDlkYmEwZTk0ZjlhZGVlMTdhNTFlN2YyYzM0NmQxNzk0ZmUwM2QzZWNlMWIyZTI3NjFiYWY0ZmUzODc3NDhjZThkMTVlMGY3ZThlNjM1NGU2NTYwYzA0ZGQ3MTQ0MmY3NDE3NGE2N2I2NzRiYzIxZDQwNGE4OGJhMGU1ZWEyNDc0NTgyYmY4NWVlNWI4YzQwZWJiZDhlN2I4MTQ5NGI1ZmE0MzIyN2E2ZmM4YmQwMmQyN2RlNmYzNjJlYWQ3NzUzMjYzNWZjMmM3OWM0YmQ0OTE4NzUwZDNjZjZkMGZkNzliOTI4NTY4Y2Q0NjE3MjYxYzEwMzk2NmYzMTNlNGQxNWEwYzRjMDE4ZGZkYmJhZDY5ZGQxYzc4MWRkMjYxMDM2M2NhYzQxZjViMjVkNmMwNDlhMDAxMzhkMjFlNWQzZDkxNWZmNTVlZGE2YTE5MGM4MTUyZjRiMWNlZWYwMTIwMjkyNDk3ZDY0NzFkY2EzOTQwMzgxMGJjNmYxMDI5NDhjNTA4Yzk1OThkMzJlYjYwMmI1ZjBmYTA5ZDAwMGFmMDRkODU4NzIyMzllOGU2YTZmNDBhYzY2NDRkY2ZkOGJmMjI1YTA0YzMyOThhYTc0NGIzZWY3YjFmMTIzMTZmYjQ0MmYyZWRjODZlZWUyZDY4ZWQ5OTFiMDRmMjI0ODVlNzYyZWNjMTcxZjFlNThkZmE3YzVjYjI3ZWFiMmY5YWM4NGFiNWJiNzhkYjljNjM2NDU4Y2Y1OGE0YzIyNzJhMzQ1OGRhMzY3ZDc5OGQxNDVlMTJjODU5YWM3MzY1M2UzNGEyZTFiZWNlOGExN2Q4YWY4ZDI4NjdmNjYyNDg3ZGMzNzk4MTk0MDZiODNlMDY4MDlhNGU1N2NmZjhkYjk4YjllNzE2YjU4MDk2ZDczOWFjY2YwNTUwZTBhZDUyZTNiYzMyMWQyNGM1ZGNlZjBlYjQxZjg1ZjI0Y2U1OWMzZjg4M2U1ZjJjZjJiZDJmMGYzYWU2ZjU0ZmU1OTgyZmRkYTQyNGUxYzZmODYzODYxYjM4NDA0NjkzYzE2MjQwYzdkY2ZlMGY4YTlhYmI1NTZkZWFiZGI5MGE0NjIxMzNlMjQ3NjZhZDU2YTZjYzEzZWM4ZWExNzI4MWFhYjE5\"}]}";

    @Before
    public void setup(){
        attachmentCreater = mock(AttachmentCreater.class);
        keywordSearchHelper = mock(KeywordSearchHelper.class);

        String testInput = "{\"objects\":[{\"success\":false,\"resultData\":null,\"completed\":null,\"expires\":\"2018-08-30T16:00:00.00Z\",\"discovered\":null,\"valid\":\"2018-08-29T14:26:00.00Z\",\"type\":\"099773ec-45e1-4957-8bc7-b394aad96955\",\"id\":\"b24a2a8e-ab96-11e8-8992-e2195faec441\",\"metadata\":\"eyJ0cmFpbGVyQ29sb3IiOiAiRkYwMDE3IiwgImNvbXBsZXRlIjogNTksICJ0b3BJbWFnZSI6ICJodHRwczovL2Muc3RhdGljLW5pa2UuY29tL2EvaW1hZ2VzL2NfbGltaXQsd18xNjAwLGhfMTYwMCxmbF9wcm9ncmVzc2l2ZSxxX2F1dG8sZl9hdXRvL2F1OGYydGxmb2Jwd3E5MDN4ZnFnL2ltYWdlLmpwZyIsICJ0aHJlYWRJZCI6ICIwMWIwMzAzYy1kZDdlLTQxY2EtODJmOC1jejU4OTY0MmFhNWEiLCAiYm91bmRzIjogWzI1LCA2NSwgNjIsIDVdLCAiaW1hZ2VJZCI6ICJodHRwczovL2Muc3RhdGljLW5pa2UuY29tL2EvaW1hZ2VzL3dfNzUwLGNfbGltaXQvbTB2dzVnMmN5Znp0Nm56b24yaWUvc2VyZW5hLXZpcmdpbC1haXItbWF4LTk3LmpwZyIsICJpbnZlbnRvcnkiOiAxMDAsICJjYXJkSWQiOiAiZDRmNjM0NTgtMzQ4MS00Y2U2LWJlMzItYWNkMDNjMmN6ZDE2IiwgImJhc2VJbWFnZSI6ICJodHRwczovL2Muc3RhdGljLW5pa2UuY29tL2EvaW1hZ2VzL2NfbGltaXQsd18xNjAwLGhfMTYwMCxmbF9wcm9ncmVzc2l2ZSxxX2F1dG8sZl9hdXRvL3FmbWc3eXpncTh3aGQ0eHg4cmJiL2ltYWdlLmpwZyJ9\"}]}";

        when(keywordSearchHelper.search("HD Cotton Rider Jacket")).thenReturn(true);

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        stockTracker = mock(StockTracker.class);
        when(stockTracker.notifyForObject("b24a2a8e-ab96-11e8-8992-e2195faec441", false)).thenReturn(true);


    //    nikeScratchResponseParser = new HuntResponseParser(stockTracker);//"https://url.com", 0, s, d, attachmentCreater, httpRequestHelper, keywordSearchHelper, stockTracker);
    }

    @Test
    public void testInStock(){
        nikeScratchResponseParser.parse(basicHttpResponse, attachmentCreater, false);
      //  Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://c.static-nike.com/a/images/c_limit,w_1600,h_1600,fl_progressive,q_auto,f_auto/au8f2tlfobpwq903xfqg/image.jpg"), eq("TOP IMAGE: SCRATCHER"), eq("Hunt"), eq(null), eq(null), eq("https://c.static-nike.com/a/images/c_limit,w_1600,h_1600,fl_progressive,q_auto,f_auto/au8f2tlfobpwq903xfqg/image.jpg"));
    //    Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq("https://c.static-nike.com/a/images/c_limit,w_1600,h_1600,fl_progressive,q_auto,f_auto/qfmg7yzgq8whd4xx8rbb/image.jpg"), eq("BASE IMAGE: SCRATCHER"), eq("Hunt"), eq(null), eq(null), eq("https://c.static-nike.com/a/images/c_limit,w_1600,h_1600,fl_progressive,q_auto,f_auto/qfmg7yzgq8whd4xx8rbb/image.jpg"));

    }

    @Test
    public void testOOS(){
        String testInput = "{\"objects\":[]}";

        when(basicHttpResponse.getBody()).thenReturn(testInput);

        nikeScratchResponseParser.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());

    }

    @Test
    public void testIsFirstTime(){
        when(stockTracker.notifyForObject("b24a2a8e-ab96-11e8-8992-e2195faec441", false)).thenReturn(false);
        nikeScratchResponseParser.parse(basicHttpResponse, attachmentCreater, false);
     //   Mockito.verify(attachmentCreater, Mockito.times(0)).addMessages(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testSnkrsPassLoad(){
        when(stockTracker.notifyForObject("644cd70c-babc-11e8-a98d-ce9105d17f52", false)).thenReturn(true);
        when(basicHttpResponse.getBody()).thenReturn(PASS);
        nikeScratchResponseParser.parse(basicHttpResponse, attachmentCreater, false);
   //     Mockito.verify(attachmentCreater, Mockito.times(1)).addMessages(eq(""), eq("NEW SNKRS HUNT LOADED: SNKRS PASS"), eq("Hunt"), eq(null), eq(null));
    }
}
