package com.restocktime.monitor.monitors.parse.target.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.target.model.Images;
import com.restocktime.monitor.monitors.parse.target.model.TargetResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.monitors.parse.target.attachment.TargetBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.net.URLEncoder;

public class TargetAbstractResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(TargetAbstractResponseParser.class);

    private ObjectMapper objectMapper;
    private StockTracker stockTracker;
    private List<String> formatNames;
    private String pid;

    public TargetAbstractResponseParser(ObjectMapper objectMapper, StockTracker stockTracker, List<String> formatNames, String pid) {
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.pid = pid;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        if(basicHttpResponse.getBody().get().trim().equals("{\"message\":\"no products found\"}")){
            stockTracker.setOOS(pid);
            logger.info("not found - " + pid);
        }

        try {

            TargetResponse targetResponse = objectMapper.readValue(basicHttpResponse.getBody().get(), TargetResponse.class);
            int stock = targetResponse.getProduct().getAvailable_to_promise_network().getOnline_available_to_promise_quantity();
            String title = targetResponse.getProduct().getItem().getProduct_description().getTitle();
            String product_url = targetResponse.getProduct().getItem().getBuy_url();
            String price = targetResponse.getProduct().getPrice().getListPrice().getFormattedPrice();
            String image_url = "";
            for(Images targetImage : targetResponse.getProduct().getItem().getEnrichment().getImages()){
                image_url = String.format("https://target.scene7.com/is/image/Target/%s", targetImage.getPrimary());
            }
            String result = title.replaceAll("[-+.^:!)(,]","");
            result = result.toLowerCase();
            String ebay = String.format("https://www.ebay.com/sch/i.html?_nkw=%s&rt=nc&LH_Sold=1&LH_Complete=1", URLEncoder.encode(result, "UTF-8"));
            String stash = String.format("https://stashpedia.com/search?terms=%s", URLEncoder.encode(result, "UTF-8"));
            //System.out.println(title + " " + price + " " + stock + " available. " + product_url + " " + image_url);
            if(targetResponse.getProduct().getAvailable_to_promise_network().getAvailability_status().equals("IN_STOCK") && stock > 0 && stockTracker.notifyForObject(pid, isFirst))
            {
                TargetBuilder.buildAttachments(attachmentCreater, product_url, image_url, title, price, stock, ebay, stash, formatNames);
            } else if (targetResponse.getProduct().getAvailable_to_promise_network().getAvailability_status().equals("OUT_OF_STOCK") && stock == 0) {
                stockTracker.setOOS(pid);
                logger.info("OOS - " + pid);
            }
//            for(TargetProduct targetProduct : targetResponse.getProducts()) {
//                if (targetProduct.getOnlineInfo().getAvailabilityCode().equals("IN_STOCK") && stockTracker.notifyForObject(pid, isFirst)) {
//                    TargetBuilder.buildAttachments(attachmentCreater, targetProduct.getTargetDotComUri(), targetProduct.getImages().getPrimaryUri(), targetProduct.getTitle(), targetProduct.getOnlineInfo().getPrice().getCurrentPriceText(), formatNames);
//                } else if(targetProduct.getOnlineInfo().getAvailabilityCode().equals("OUT_OF_STOCK")) {
//                    stockTracker.setOOS(pid);
//                    logger.info("OOS - " + pid);
//                }
//            }

        } catch (Exception e){

        }
    }
}