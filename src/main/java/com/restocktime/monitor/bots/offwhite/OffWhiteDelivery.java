package com.restocktime.monitor.bots.offwhite;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import org.apache.log4j.Logger;

public class OffWhiteDelivery {
    final static Logger logger = Logger.getLogger(OffWhiteDelivery.class);

    private final String DELIVERY_URL_TEMPLATE = "";
    private final String DELIVERY_BODY_TEMPLATE = "utf8=%E2%9C%93&" +
            "_method=patch&authenticity_token=BRQ2uXfKobHS%2BRz1A%2FrHB8QrZI769UFsufQuYoxsLms%3D" +
            "&order%5Bstate_lock_version%5D=4" +
            "&order%5Bshipments_attributes%5D%5B0%5D%5Bselected_shipping_rate_id%5D=371999" +
            "&order%5Bshipments_attributes%5D%5B0%5D%5Bid%5D=274326" +
            "&commit=Save+and+Continue";

    public OffWhiteDelivery() {

    }

    public void submitDelivery(BasicRequestClient basicRequestClient, CloudflareRequestHelper cloudflareRequestHelper, String region) {
        String deliveryUrl = String.format(DELIVERY_URL_TEMPLATE, region);
        String deliveryBody = DELIVERY_BODY_TEMPLATE;

        logger.info(deliveryUrl);

        while (true) {
            BasicHttpResponse basicHttpResponse = cloudflareRequestHelper.performPost(basicRequestClient, deliveryUrl, deliveryBody);
            logger.info(basicHttpResponse.getBody());
            if (basicHttpResponse == null || basicHttpResponse.getBody() == null) {

                continue;
            } else if (basicHttpResponse.getResponseCode() >= 400) {
                logger.info("Site error or cloudflare");
            } else if (basicHttpResponse.getBody().contains("checkout-step-delivery")) {
                logger.info("Wrong pass");
            } else if (basicHttpResponse.getBody().contains("checkout-step-payment")) {
                logger.info("Successfully submitted");
                break;
            }
        }

    }
}
