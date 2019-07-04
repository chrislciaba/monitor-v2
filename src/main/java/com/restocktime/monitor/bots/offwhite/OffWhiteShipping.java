package com.restocktime.monitor.bots.offwhite;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.bots.profiles.Address;
import org.apache.log4j.Logger;

import java.net.URLEncoder;

public class OffWhiteShipping {
    final static Logger logger = Logger.getLogger(OffWhiteShipping.class);

    private final String ADDRESS_BODY_TEMPLATE = "utf8=%E2%9C%93" +
            "&_method=patch" +
            "&authenticity_token=kg93HjRA1hPQRgsbf8HSUHk%2Bl5t5ToEDONtfV%2BDFqPo%3D" +
            "&order%5Bemail%5D=%s" +
            "&order%5Bstate_lock_version%5D=0" +
            "&order%5Bbill_address_attributes%5D%5Bfirstname%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Blastname%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Baddress1%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Baddress2%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Bcity%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Bstate_id%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Bstate_name%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Bcountry_id%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Bzipcode%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Bphone%5D=%s" +
            "&order%5Bbill_address_attributes%5D%5Bhs_fiscal_code%5D=" +
            "&order%5Buse_billing%5D=1" +
            "&save_user_address=1" +
            "&order%5Bterms_and_conditions%5D=no" +
            "&order%5Bterms_and_conditions%5D=yes" +
            "&commit=Save+and+Continue";

    private final String URL_TEMPLATE = "https://www.off---white.com/en/%s/checkout/update/address";

    private Address address;

    public OffWhiteShipping(Address address) {
        this.address = address;
    }

    public void submitShipping(BasicRequestClient basicRequestClient, CloudflareRequestHelper cloudflareRequestHelper, String region){
        String body = formatAddress();
        String url = formatUrl(region);

        while(true){
            BasicHttpResponse basicHttpResponse = cloudflareRequestHelper.performPost(basicRequestClient, url, body);
            logger.info(basicHttpResponse.getBody());
            if(basicHttpResponse == null ||  basicHttpResponse.getBody() == null){

                continue;
            } else if(basicHttpResponse.getResponseCode() >= 400){
                logger.info("Site error or cloudflare");
            } else if(basicHttpResponse.getBody().contains("/en/GB/checkout/update/delivery")){
                logger.info("successfully submitted delivery");
                break;
            } else {
                logger.info("unknown error");
                logger.info(basicHttpResponse.getBody());
            }
        }
    }

    private String formatAddress(){
        return String.format(ADDRESS_BODY_TEMPLATE,
                format(address.getEmail()),
                format(address.getFirstName()),
                format(address.getLastName()),
                format(address.getAddress1()),
                format(address.getAddress2()),
                format(address.getCity()),
                getStateId(address.getState()),
                format(address.getState()),
                getCountryId(address.getCountry()),
                format(address.getZip()),
                format(address.getPhone())
        );
    }

    private String formatUrl(String region){
        return String.format(URL_TEMPLATE, region);
    }

    private String format(String original){
        if(original == null){
            return "";
        }

        String[] words = original.replaceAll("\\s+", " ").split(" ");
        String ret = "";
        for(int i = 0; i < words.length; i++){
            try {
                ret = ret + (i > 0 ? "+" : "") + URLEncoder.encode(words[i], "UTF-8");
            } catch(Exception e){

            }
        }

        return ret;
    }

    private String getStateId(String state){
        return "";
    }

    private String getCountryId(String country){
        return "44";
    }



}
