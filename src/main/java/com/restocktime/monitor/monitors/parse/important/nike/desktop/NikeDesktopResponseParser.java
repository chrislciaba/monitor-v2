package com.restocktime.monitor.monitors.parse.important.nike.desktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.nike.desktop.attachment.NikeBuilder;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.attachment.SnkrsBuilder;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.ParsedResponse;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2.ProductFeed;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.helper.ParseV2Helper;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class NikeDesktopResponseParser implements AbstractResponseParser {
    private ObjectMapper objectMapper;
    private StockTracker stockTracker;
    private ParseV2Helper parseV2Helper;
    private List<String> formatNames;
    private final String PRODUCT_LINK_TEMPLATE = "https://nike.com/t/%s/%s";
    private final String IMG_TEMPLATE = "https://secure-images.nike.com/is/image/DotCom/%s";

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        try {
            ProductFeed productFeed = objectMapper.readValue(basicHttpResponse.getBody().get(), ProductFeed.class);
            ParsedResponse parsedResponse = parseV2Helper.getParsedResponse(productFeed.getObjects().get(0));
            if (parsedResponse.isAvailable() && stockTracker.notifyForObject(parsedResponse.getId(), isFirst)) {
                String link = getProductUrl(parsedResponse.getSlug(), parsedResponse.getSku());
                String imgUrl = parsedResponse.getSku() == null ? null : String.format(IMG_TEMPLATE, parsedResponse.getSku().replace("-", "_"));
                NikeBuilder.buildAttachments(attachmentCreater, link, imgUrl, parsedResponse.getName(), parsedResponse.getSizeAndStocks(), parsedResponse.getSku(), formatNames);
            } else if (!parsedResponse.isAvailable()) {
                stockTracker.setOOS(parsedResponse.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getProductUrl(String slug, String sku) {
        return String.format(PRODUCT_LINK_TEMPLATE, slug, sku);

    }
}
