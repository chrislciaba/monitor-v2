package com.restocktime.monitor.monitors.parse.ssense.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.ssense.model.ssense.Product;
import com.restocktime.monitor.monitors.parse.ssense.model.ssense.SsenseSearch;
import com.restocktime.monitor.monitors.ingest.ssense.Ssense;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;

public class SearchResponseParser implements AbstractResponseParser {
    private String locale;
    private String url;
    private String productName;
    final static Logger logger = Logger.getLogger(Ssense.class);
    private StockTracker stockTracker;
    private List<String> formatNames;

    public SearchResponseParser(String locale, String url, String productName, StockTracker stockTracker, List<String> formatNames) {
        this.locale = locale;
        this.url = url;
        this.productName = productName;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if(basicHttpResponse == null || basicHttpResponse.getBody() == null){
        }

        String responseString = basicHttpResponse.getBody();

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

        }
    }

}
