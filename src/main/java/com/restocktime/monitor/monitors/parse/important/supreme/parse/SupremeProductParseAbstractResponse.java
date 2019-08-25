package com.restocktime.monitor.monitors.parse.important.supreme.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.supreme.attachment.SupremeBuilder;
import com.restocktime.monitor.monitors.parse.important.supreme.model.supreme.Product;
import com.restocktime.monitor.monitors.parse.important.supreme.model.supreme.Size;
import com.restocktime.monitor.monitors.parse.important.supreme.model.supreme.Style;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class SupremeProductParseAbstractResponse implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(SupremeProductParseAbstractResponse.class);

    private StockTracker stockTracker;
    private String url;
    private String name;
    private String locale;

    private final String SUP_COLOR_URL_TEMPLATE = "%s/%s";
    private List<String> formatNames;

    public SupremeProductParseAbstractResponse(StockTracker stockTracker, String url, String name, String locale, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.name = name;
        this.locale = locale;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product supremeProduct = objectMapper.readValue(responseString, Product.class);


            for (Style style : supremeProduct.getStyles()) {
                List<String> sizes = new ArrayList<>();

                for (Size size : style.getSizes()) {
                    if (size.getStock_level() != 0 && stockTracker.notifyForObject(style.getId() + ":" + size.getName(), false)) {
                        sizes.add(size.getName());
                    } else if (size.getStock_level() == 0) {
                        stockTracker.setOOS(style.getId() + ":" + size.getName());
                    }
                }

                if (sizes.size() > 0) {
                    SupremeBuilder.buildAttachments(attachmentCreater, String.format(SUP_COLOR_URL_TEMPLATE, url, style.getId()), "https:" + style.getImage_url(), locale, name, style.getName(), "", sizes, formatNames);
                } else {
                    logger.info("oos - " + url);
                }

            }
        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);

        }

    }

}
