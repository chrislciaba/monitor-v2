package com.restocktime.monitor.monitors.parse.ssense.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.ssense.model.ssense.Product;
import com.restocktime.monitor.monitors.parse.ssense.model.ssense.SsenseSearch;
import com.restocktime.monitor.monitors.ingest.ssense.Ssense;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class SearchResponseParser implements AbstractResponseParser {

    final static Logger logger = Logger.getLogger(Ssense.class);
    private StockTracker stockTracker;
    private List<String> formatNames;

    public SearchResponseParser(String locale, String url, String productName, StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SsenseSearch ssenseSearch = objectMapper.readValue(responseString, SsenseSearch.class);
            if (ssenseSearch.getProducts() == null || ssenseSearch.getProducts().size() == 0) {
                return;
            }

            for(Product p : ssenseSearch.getProducts()) {
                if (!stockTracker.notifyForObject(p.getUrl(), isFirst)) {
                    continue;
                }

              //  attachmentCreater.addMessages(url, productName, "ssense", null, null);
            }

        } catch(Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);

        }
    }

}
