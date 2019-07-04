package com.restocktime.monitor.monitors.nikescratch;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.monitors.ingest.snkrs.NikeScratch;
import com.restocktime.monitor.monitors.parse.snkrs.parse.HuntResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.client.SmsClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Notifications.class)
public class NikeScratchTest {
    private NikeScratch nikeScratch;
    private HttpRequestHelper httpRequestHelper;
    private AttachmentCreater attachmentCreater;
    private CloseableHttpClient closeableHttpClient;
    private RequestConfig requestConfig;
    private SlackObj[] s;
    private String[] d;
    private Map<String, Integer> alreadyChecked;
    private SmsClient smsClient;
    private BasicHttpResponse basicHttpResponse;
    private BasicRequestClient basicRequestClient;
    private HuntResponseParser huntResponseParser;
    private Notifications notifications;

    @Before
    public void setup(){
        basicRequestClient = mock(BasicRequestClient.class);
        notifications = mock(Notifications.class);
        httpRequestHelper = mock(HttpRequestHelper.class);
        attachmentCreater = mock(AttachmentCreater.class);
        closeableHttpClient = mock(CloseableHttpClient.class);
        requestConfig = mock(RequestConfig.class);
        smsClient = mock(SmsClient.class);
        huntResponseParser = mock(HuntResponseParser.class);

        String testInput = "{\"objects\":[{\"success\":false,\"resultData\":null,\"completed\":null,\"expires\":\"2018-08-30T16:00:00.00Z\",\"discovered\":null,\"valid\":\"2018-08-29T14:26:00.00Z\",\"type\":\"099773ec-45e1-4957-8bc7-b394aad96955\",\"id\":\"b24a2a8e-ab96-11e8-8992-e2195faec441\",\"metadata\":\"eyJ0cmFpbGVyQ29sb3IiOiAiRkYwMDE3IiwgImNvbXBsZXRlIjogNTksICJ0b3BJbWFnZSI6ICJodHRwczovL2Muc3RhdGljLW5pa2UuY29tL2EvaW1hZ2VzL2NfbGltaXQsd18xNjAwLGhfMTYwMCxmbF9wcm9ncmVzc2l2ZSxxX2F1dG8sZl9hdXRvL2F1OGYydGxmb2Jwd3E5MDN4ZnFnL2ltYWdlLmpwZyIsICJ0aHJlYWRJZCI6ICIwMWIwMzAzYy1kZDdlLTQxY2EtODJmOC1jejU4OTY0MmFhNWEiLCAiYm91bmRzIjogWzI1LCA2NSwgNjIsIDVdLCAiaW1hZ2VJZCI6ICJodHRwczovL2Muc3RhdGljLW5pa2UuY29tL2EvaW1hZ2VzL3dfNzUwLGNfbGltaXQvbTB2dzVnMmN5Znp0Nm56b24yaWUvc2VyZW5hLXZpcmdpbC1haXItbWF4LTk3LmpwZyIsICJpbnZlbnRvcnkiOiAxMDAsICJjYXJkSWQiOiAiZDRmNjM0NTgtMzQ4MS00Y2U2LWJlMzItYWNkMDNjMmN6ZDE2IiwgImJhc2VJbWFnZSI6ICJodHRwczovL2Muc3RhdGljLW5pa2UuY29tL2EvaW1hZ2VzL2NfbGltaXQsd18xNjAwLGhfMTYwMCxmbF9wcm9ncmVzc2l2ZSxxX2F1dG8sZl9hdXRvL3FmbWc3eXpncTh3aGQ0eHg4cmJiL2ltYWdlLmpwZyJ9\"}]}";

        basicHttpResponse = mock(BasicHttpResponse.class);
        when(basicHttpResponse.getBody()).thenReturn(testInput);
        when(basicHttpResponse.getResponseCode()).thenReturn(200);
        when(httpRequestHelper.performGet(eq(basicRequestClient), eq("https://url.com"))).thenReturn(basicHttpResponse);
        s = new SlackObj[0];
        d = new String[0];
        PowerMockito.mockStatic(Notifications.class);
        when(attachmentCreater.isEmpty()).thenReturn(false);
        alreadyChecked = new HashMap<>();
        String login = "{\"keepMeLoggedIn\":true,\"client_id\":\"PbCREuPr3iaFANEDjtiEzXooFl7mXGQ7\",\"ux_id\":\"com.nike.commerce.snkrsv2.droid\",\"grant_type\":\"password\",\"username\":\"chrislciaba@gmail.com\",\"password\":\"Kingsmma1*\"}";

        when(httpRequestHelper.performPost(eq(basicRequestClient), eq("https://api.nike.com/idn/shim/oauth/2.0/token"), eq(login))).thenReturn(new BasicHttpResponse("{\"access_token\":\"1234\"}", 200));


        //nikeScratch = new NikeScratch("https://url.com", 0, notifications, attachmentCreater, httpRequestHelper, huntResponseParser);
    }

    @Test
    public void testInput(){
        nikeScratch.run(basicRequestClient, false);
        Mockito.verify(httpRequestHelper, Mockito.times(1)).performGet(eq(basicRequestClient), eq("https://url.com"));
        Mockito.verify(huntResponseParser, Mockito.times(1)).parse(basicHttpResponse, attachmentCreater, false);
        Mockito.verify(notifications, Mockito.times(1)).send(attachmentCreater);
    }




}
