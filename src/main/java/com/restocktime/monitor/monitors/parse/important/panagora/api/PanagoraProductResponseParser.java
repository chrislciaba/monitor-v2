package com.restocktime.monitor.monitors.parse.important.panagora.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.constants.Constants;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.panagora.api.model.PanagoraProductModel;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class PanagoraProductResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(PanagoraProductResponseParser.class);


    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;
    private ObjectMapper objectMapper;

    public PanagoraProductResponseParser(StockTracker stockTracker, String url, ObjectMapper objectMapper, List<String> formatNames) {
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

        try {
            PanagoraProductModel panagoraProductModel = objectMapper.readValue(responseString, PanagoraProductModel.class);
            if(panagoraProductModel.getIsAvailable() != null && panagoraProductModel.getIsAvailable().equals("True")){
                if(stockTracker.notifyForObject(url, isFirst)){
                    DefaultBuilder.buildAttachments(attachmentCreater, url + panagoraProductModel.getUrl(), panagoraProductModel.getImage(), UrlHelper.getHost(url), panagoraProductModel.getName(), formatNames);
                }
            } else if(panagoraProductModel.getIsAvailable() != null && panagoraProductModel.getIsAvailable().equals("False")) {
                stockTracker.setOOS(url);
            } else {
                if(panagoraProductModel.getIsAvailable() == null)
                    logger.error("Not sure what happened but failed " + responseString);
            }
        } catch (IOException e) {
            logger.error(Constants.EXCEPTION_LOG_MESSAGE, e);
        }
    }
}
