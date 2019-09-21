package com.restocktime.monitor.util.captcha.anti;

import com.amazonaws.services.s3.Headers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.captcha.anti.model.*;
import com.restocktime.monitor.util.helper.timeout.Timeout;
import com.restocktime.monitor.util.ops.log.DiscordLog;
import com.restocktime.monitor.util.ops.log.WebhookType;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

@AllArgsConstructor
public class AntiCapService {
    private String apiKey;
    private String googleKey;
    private String pageUrl;
    private CloseableHttpClient closeableHttpClient;
    private ObjectMapper objectMapper;

    public String getCap() {
        try {


            long t0 = System.currentTimeMillis();
            RequestObject requestObject = RequestObject.builder()
                    .clientKey(apiKey)
                    .task(
                            RequestTask.builder()
                                    .websiteKey(googleKey)
                                    .websiteURL(pageUrl)
                                    .type("NoCaptchaTaskProxyless")
                                    .build()
                    )
                    .languagePool("en")
                    .softId(0)
                    .build();
            HttpPost httpPost = new HttpPost("https://api.anti-captcha.com/createTask");
            httpPost.setEntity(new StringEntity(
                    objectMapper.writeValueAsString(requestObject)
            ));

            httpPost.setHeader(new BasicHeader(Headers.CONTENT_TYPE, "application/json"));
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            Integer taskId = null;
            ResponseObject responseObject = objectMapper.readValue(EntityUtils.toString(httpResponse.getEntity()), ResponseObject.class);
            if(responseObject.getErrorId() > 0 || responseObject.getTaskId() == null){
                System.out.println("error: " + responseObject.getErrorId());
            } else {
                taskId = responseObject.getTaskId();
            }
            String soln = null;
            String respObj = objectMapper.writeValueAsString(
                    SolutionRequest.builder()
                            .clientKey(apiKey)
                            .taskId(taskId)
                            .build()
            );

            do {
                Timeout.timeout(5000);

                httpPost = new HttpPost("https://api.anti-captcha.com/getTaskResult");
                httpPost.setEntity(new StringEntity(respObj));
                httpResponse = closeableHttpClient.execute(httpPost);
                ResponseObject solution = objectMapper.readValue(EntityUtils.toString(httpResponse.getEntity()), ResponseObject.class);
                if(solution.getSolution() != null){
                    soln = solution.getSolution().getGRecaptchaResponse();
                }
            } while (soln == null);

            long t1 = System.currentTimeMillis();
            DiscordLog.log(WebhookType.SNS, "Cap solve time in seconds: " + Double.toString((t1 - t0) / 1000));
            return soln;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
