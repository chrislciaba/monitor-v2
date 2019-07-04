package com.restocktime.monitor.monitors.parse.offwhite.aco;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;

public class SubmitBillShip {
    String url = "https://www.off---white.com/en/AT/checkout/address";
    String body = "utf8=%E2%9C%93&_method=patch&authenticity_token=hHjH58iGHH9uEw7SZmmFtLG6wkXeAou0ohqK%2FeIq%2FLM%3D&order%5Bemail%5D=chrislciaba%40gmail.com&order%5Bstate_lock_version%5D=6&order%5Bbill_address_attributes%5D%5Bfirstname%5D=Christian&order%5Bbill_address_attributes%5D%5Blastname%5D=CIabattoni&order%5Bbill_address_attributes%5D%5Baddress1%5D=708+S+Barrington+Ave+111&order%5Bbill_address_attributes%5D%5Baddress2%5D=&order%5Bbill_address_attributes%5D%5Bcity%5D=Los+Angeles&order%5Bbill_address_attributes%5D%5Bcountry_id%5D=49&order%5Bbill_address_attributes%5D%5Bstate_id%5D=87&order%5Bbill_address_attributes%5D%5Bzipcode%5D=90049&order%5Bbill_address_attributes%5D%5Bphone%5D=2136053851&order%5Bbill_address_attributes%5D%5Bhs_fiscal_code%5D=&order%5Bbill_address_attributes%5D%5Bid%5D=1061301&order%5Bship_address_attributes%5D%5Bfirstname%5D=Eva&order%5Bship_address_attributes%5D%5Blastname%5D=Ciabattoni&order%5Bship_address_attributes%5D%5Baddress1%5D=Zollergasse+4%2F3&order%5Bship_address_attributes%5D%5Baddress2%5D=&order%5Bship_address_attributes%5D%5Bcity%5D=Vienna&order%5Bship_address_attributes%5D%5Bcountry_id%5D=111&order%5Bship_address_attributes%5D%5Bzipcode%5D=1070&order%5Bship_address_attributes%5D%5Bphone%5D=2136053851&order%5Bship_address_attributes%5D%5Bshipping%5D=true&order%5Bship_address_attributes%5D%5Bid%5D=1061302&save_user_address=1&order%5Bterms_and_conditions%5D=no&order%5Bterms_and_conditions%5D=yes&commit=Save+and+Continue";

    public void submitBillShip(BasicRequestClient basicRequestClient, CloudflareRequestHelper cloudflareRequestHelper){
        while(true){
            BasicHttpResponse basicHttpResponse = cloudflareRequestHelper.performPost(basicRequestClient, url, body);
            if(basicHttpResponse.getBody() != null && basicHttpResponse.getBody().contains("<h5 class='stock-shipping-method-title'>Shipping Method</h5>")){
                break;
            }
        }
    }
}
