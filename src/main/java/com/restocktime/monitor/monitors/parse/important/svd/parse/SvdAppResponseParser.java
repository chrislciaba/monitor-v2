package com.restocktime.monitor.monitors.parse.important.svd.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.constants.Constants;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.svd.model.SvdAppProduct;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.ops.log.DiscordLog;
import com.restocktime.monitor.util.ops.log.WebhookType;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class SvdAppResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SvdAppResponseParser.class);
    private StockTracker stockTracker;
    private List<String> formatNames;
    private ObjectMapper objectMapper;

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            DiscordLog.log(WebhookType.SVD, "Error");

            return;
        }

        try {
            SvdAppProduct svdAppProduct = objectMapper.readValue(basicHttpResponse.getBody().get(), SvdAppProduct.class);

            if (svdAppProduct.getViewType().equals("product") && !svdAppProduct.getGeneralData().getSoldout() && stockTracker.notifyForObject(svdAppProduct.getGeneralData().getSku(), isFirst)) {
                DefaultBuilder.buildAttachments(
                        attachmentCreater,
                        svdAppProduct.getGeneralData().getUrl(),
                        svdAppProduct.getGeneralData().getImages().size() > 0 ? svdAppProduct.getGeneralData().getImages().get(0) : null,
                        "SVD App (open on mobile)",
                        svdAppProduct.getGeneralData().getBrand().toUpperCase() + " " + svdAppProduct.getGeneralData().getName() + " - " + svdAppProduct.getGeneralData().getSku(),
                        formatNames
                );
            } else if (svdAppProduct.getViewType().equals("product") && svdAppProduct.getGeneralData().getSoldout()) {
                stockTracker.setOOS(svdAppProduct.getGeneralData().getSku());
            }

            DiscordLog.log(WebhookType.SVD, "Success");

        } catch(IOException e) {
            DiscordLog.log(WebhookType.SVD, "IOError: " + e.getMessage());

            logger.error("Failed to parse svd response", e);
        } catch (Exception e) {
            DiscordLog.log(WebhookType.SVD, "Error: " + e.getMessage());

            logger.error(Constants.EXCEPTION_LOG_MESSAGE, e);
        }
    }
}
