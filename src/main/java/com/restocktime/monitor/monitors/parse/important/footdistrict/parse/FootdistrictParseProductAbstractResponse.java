package com.restocktime.monitor.monitors.parse.important.footdistrict.parse;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FootdistrictParseProductAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(FootdistrictParseProductAbstractResponse.class);

    private StockTracker stockTracker;
    private String url;
    private String patternStr = "<title>(.*)</title>";
    private Pattern pattern;
    private List<String> formatNames;

    public FootdistrictParseProductAbstractResponse(StockTracker stockTracker, String url, List<String> formatNames){
        this.url = url;
        this.stockTracker = stockTracker;
        pattern = Pattern.compile(patternStr);
        this.formatNames = formatNames;
    }


    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        if(responseString.contains("productAddToCartForm.submit(this)")){
            String title = url;
            logger.info("product in stock");
            Matcher m = pattern.matcher(responseString);
            if(m.find()){
                title = m.group(1);
            }

            if(stockTracker.notifyForObject(url, isFirst)){
            //    attachmentCreater.addMessages(url, title, "FootDistrict", null, null);
                DefaultBuilder.buildAttachments(attachmentCreater, url, null,"FootDistrict", title, formatNames);

            }
        } else if(responseString.contains("out-of-stock")){
            logger.info("oos");
            stockTracker.setOOS(url);
        } else {
            logger.info("weird shit");
        }
    }
}
