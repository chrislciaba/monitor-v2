package com.restocktime.monitor.monitors.parse.aio.lvr.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.aio.lvr.model.Product;
import com.restocktime.monitor.monitors.parse.aio.lvr.model.ProductList;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LvrResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(LvrResponseParser.class);
    private Pattern pattern;
    private Pattern jsonPattern = Pattern.compile("var tevent = ([^;]);");
    private String LVR_PRODUCT_TEMPLATE = "https://www.luisaviaroma.com%s";
    private String LVR_IMAGE_TEMPLATE = "https://images.lvrcdn.com/%s";

    private StockTracker stockTracker;
    private String url;
    private List<String> formatNames;
    private ObjectMapper objectMapper;

    public LvrResponseParser(StockTracker stockTracker, String url, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.url = url;
        this.formatNames = formatNames;
        objectMapper = new ObjectMapper();
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        Matcher jsonMatcher = jsonPattern.matcher(responseString);
        logger.info(responseString);

        if(jsonMatcher.find()){
            logger.info("found");
            try {
                ProductList productList = objectMapper.readValue(jsonMatcher.group(1), ProductList.class);
                for(Product product : productList.getProductList()){
                   // if(stockTracker.notifyForObject(product.getItemId(), false)){
                        String url = String.format(LVR_PRODUCT_TEMPLATE, product.getItemId());
                        String name = product.getDesignerDescription() + " - " + product.getDescription();
                        String image = String.format(LVR_IMAGE_TEMPLATE, product.getImagePath());
                        logger.info("Loaded - " + url);
                        DefaultBuilder.buildAttachments(attachmentCreater, url, image, "LVR", name, formatNames);
                        break;
                   // }
                }
            } catch (Exception e){

            }
        }
    }
}
