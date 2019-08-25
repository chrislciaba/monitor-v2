package com.restocktime.monitor.monitors.parse.important.offspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.offspring.model.OffspringObj;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Offspring implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(Offspring.class);

    private final String productTemplate = "QLTracking\\.listing\\.items\\.push\\(([^)]*)\\)";

    private Pattern productPattern;
    private List<String> formatNames;
    private final String URL_TEMPLATE = "https://www.offspring.co.uk/view/product/offspring_catalog/1,21/%s";
    private ObjectMapper objectMapper;
    private StockTracker stockTracker;

    public Offspring(StockTracker stockTracker, List<String> formatNames, ObjectMapper objectMapper){
        this.productPattern = Pattern.compile(productTemplate);
        this.formatNames = formatNames;
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        Matcher m = productPattern.matcher(basicHttpResponse.getBody().get());

        while(m.find()){
            try {
                String sanitized = m.group(1).replaceAll("\\s+", " ").replaceAll(",\\s*([A-Za-z0-9_]+)\\s*:", ",\"$1\":")
                        .replaceAll("\\{\\s*([A-Za-z0-9_]+)\\s*:", "{\"$1\":");
                OffspringObj offspringObj = objectMapper.readValue(sanitized, OffspringObj.class);
                if(stockTracker.notifyForObject(offspringObj.getSku_code(), isFirst)){
                    DefaultBuilder.buildAttachments(
                            attachmentCreater,
                            String.format(URL_TEMPLATE, offspringObj.getSku_code()),
                            null,
                            "OffSpring",
                            offspringObj.getName(),
                            formatNames
                    );
                }
            } catch (Exception e){
                logger.error(EXCEPTION_LOG_MESSAGE, e);
            }
        }
    }
}
