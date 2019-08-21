package com.restocktime.monitor.monitors.parse.sns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.monitors.parse.sns.attachment.SnsBuilder;
import com.restocktime.monitor.monitors.parse.sns.model.SizeObj;
import com.restocktime.monitor.monitors.parse.sns.model.SnsSizeModel;
import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SNSProductResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(SnsResponseParser.class);
    private StockTracker stockTracker;
    private List<String> formatNames;
    private final Pattern titlePattern = Pattern.compile("<title>([^<]*)</title>");
    private final Pattern imgPattern = Pattern.compile("<img\\s+class=\"card-img \"\\s+src=\"([^\"]*)\"");

    private final Pattern sizePattern = Pattern.compile("for=\"product-([^\"]*)\"\\s+data-size-types='([^']*)'\\s+class=\"product-sizes__label\"");
    private final String SNS_TEMPLATE = "https://www.sneakersnstuff.com%s";
    private String url;
    private ObjectMapper objectMapper;


    public SNSProductResponseParser(StockTracker stockTracker, String url, List<String> formatNames, ObjectMapper objectMapper) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
        this.objectMapper = objectMapper;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        List<SizeObj> sizes = new ArrayList<>();
        Matcher sizeMatcher = sizePattern.matcher(responseString);
        while (sizeMatcher.find()) {
            try {
                SnsSizeModel snsSizeModel = objectMapper.readValue(sizeMatcher.group(2), SnsSizeModel.class);
                sizes.add(
                        SizeObj.builder()
                                .size(snsSizeModel.getUs())
                                .sku(sizeMatcher.group(1))
                                .build()
                );
            } catch (IOException e) {
                logger.info("Failed to read", e);
            }
        }

        if(!sizes.isEmpty()){
            if(stockTracker.notifyForObject(url, isFirst)) {
                String title = "HYPE SHIT";
                Matcher m = titlePattern.matcher(responseString);
                if(m.find())
                    title = m.group(1);

                Matcher imgMatch = imgPattern.matcher(responseString);
                String imgUrl = null;
                if(imgMatch.find()){
                    imgUrl = imgMatch.group(1);
                }

                SnsBuilder.buildAttachments(attachmentCreater, url, title, String.format(SNS_TEMPLATE, imgUrl), sizes, formatNames);
            }

        } else if(responseString.contains("Sold out") || responseString.contains("This raffle is closed") || responseString.contains("<meta name=\"author\" content=\"Sneakersnstuff\">")){
            stockTracker.setOOS(url);

        }
    }
}
