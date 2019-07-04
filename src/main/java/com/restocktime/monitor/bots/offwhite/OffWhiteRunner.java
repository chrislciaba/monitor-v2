package com.restocktime.monitor.bots.offwhite;

import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;

public class OffWhiteRunner {

    private OffWhiteLogin offWhiteLogin;
    private OffWhiteShipping offWhiteShipping;
    private OffWhitePayment offWhitePayment;

    public OffWhiteRunner(OffWhiteLogin offWhiteLogin, OffWhiteShipping offWhiteShipping, OffWhitePayment offWhitePayment) {
        this.offWhiteLogin = offWhiteLogin;
        this.offWhiteShipping = offWhiteShipping;
        this.offWhitePayment = offWhitePayment;
    }

    public void startTask(BasicRequestClient basicRequestClient, CloudflareRequestHelper cloudflareRequestHelper, String region){
        offWhiteLogin.login(basicRequestClient, cloudflareRequestHelper, region);
        offWhiteShipping.submitShipping(basicRequestClient, cloudflareRequestHelper, region);
    }
}
