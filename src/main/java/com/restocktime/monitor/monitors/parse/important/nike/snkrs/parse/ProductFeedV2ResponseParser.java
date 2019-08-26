package com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.attachment.SnkrsBuilder;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.helper.ParseExpHelper;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.helper.ParseV1Helper;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.helper.ParseV2Helper;
import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.exp.ExpSnkrs;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv1.Content;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2.ProductFeed;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.ParsedResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.*;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ProductFeedV2ResponseParser implements AbstractResponseParser {
    private ObjectMapper objectMapper;
    final static Logger logger = Logger.getLogger(ProductFeedV2ResponseParser.class);
    private StockTracker stockTracker;
    private String region;
    private String LINK;
    private List<String> formatNames;
    private final String dateFormat = "<!date^%s^{date_num} {time_secs}|N/A>";
    private Map<String, Long> skus;
    private final String LINK_UK = "https://www.nike.com/gb/launch";
    private final String LINK_JP = "https://www.nike.com/jp/launch";
    private final String LINK_CN = "https://www.nike.com/cn/launch";
    private final String LINK_US = "https://nike.com/launch";
    private final String IMG_TEMPLATE = "https://secure-images.nike.com/is/image/DotCom/%s";
    private final String PRODUCT_LINK_TEMPLATE = "%s/t/%s";
    private String url;

    public ProductFeedV2ResponseParser(ObjectMapper objectMapper, StockTracker stockTracker, String region, List<String> formatNames, String url) {
        this.objectMapper = objectMapper;
        this.stockTracker = stockTracker;
        this.region = region;
        if(region.equals("US")){
            this.LINK = LINK_US;
        } else if(region.equals("UK")){
            this.LINK = LINK_UK;
        } else if(region.equals("JP")){
            this.LINK = LINK_JP;
        } else if(region.equals("CN")){
            this.LINK = LINK_CN;
        } else {
            this.LINK = LINK_US;
        }
        this.skus = skus;
        this.formatNames = formatNames;
        this.url = url;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        List<ParsedResponse> products = getParsedResponseList(basicHttpResponse.getBody().get());

        if(products == null){
            return;
        }

        for(ParsedResponse parsedResponse : products){
            if(!stockTracker.notifyForObject(parsedResponse.getId(), isFirst) && parsedResponse.isAvailable()){
                continue;
            }

            String link = getProductUrl(parsedResponse.getSlug());
            String launchEpochStr = null;

            if(parsedResponse.getLaunchDateEpoch() != null){
                launchEpochStr = String.format(dateFormat, parsedResponse.getLaunchDateEpoch());
            }

            String imgUrl = parsedResponse.getSku() == null ? null : String.format(IMG_TEMPLATE, parsedResponse.getSku().replace("-", "_"));
            SnkrsBuilder.buildAttachments(attachmentCreater, link, imgUrl, parsedResponse.getName(), parsedResponse.getType(), parsedResponse.getSelectionEngine(), launchEpochStr, parsedResponse.getLaunchDate(), parsedResponse.getSku(), region, formatNames);
        }

        if(attachmentCreater.isEmpty()){
            logger.info("No new products - " + region);
        }
    }

    private String getProductUrl(String slug) {
        if(slug == null){
            return LINK;
        }

        String url = String.format(PRODUCT_LINK_TEMPLATE, LINK, slug);

        if(UrlHelper.isValidUrl(url)){
            return url;
        }

        return LINK;
    }


    private List<ParsedResponse> getParsedResponseList(String responseStr){
        if (responseStr.contains("product_feed/threads/v2")) {
            try {
                ProductFeed productFeed = objectMapper.readValue(responseStr, ProductFeed.class);
                ParseV2Helper parseV2Helper = new ParseV2Helper();
                return parseV2Helper.getParsedResponseList(productFeed);

            } catch(Exception e){
                logger.error(EXCEPTION_LOG_MESSAGE, e);
                return null;
            }
        } else if(url.contains("exp_snkrs")){
            logger.info("exp");
            try {
                ExpSnkrs expSnkrs = objectMapper.readValue(responseStr, ExpSnkrs.class);
                ParseExpHelper parseExpHelper = new ParseExpHelper(expSnkrs);
                return parseExpHelper.getParsedResponseList();
            } catch (Exception e){
                logger.error(EXCEPTION_LOG_MESSAGE, e);
                return null;
            }
        } else {
            try {
                Content content = objectMapper.readValue(responseStr, Content.class);
                ParseV1Helper parseV1Helper = new ParseV1Helper(content);
                return parseV1Helper.getParsedResponseList();
            } catch (Exception e){
                logger.error(EXCEPTION_LOG_MESSAGE, e);
                return null;
            }
        }
    }
}
