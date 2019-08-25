package com.restocktime.monitor.monitors.parse.mesh.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.log.DiscordLog;
import com.restocktime.monitor.util.log.WebhookType;
import com.restocktime.monitor.util.metrics.MonitorMetrics;
import com.restocktime.monitor.util.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.mesh.attachment.FootpatrolBuilder;
import com.restocktime.monitor.monitors.parse.mesh.model.app.FpOption;
import com.restocktime.monitor.monitors.parse.mesh.model.app.FpProduct;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;


public class MeshApp implements AbstractResponseParser {
    private static final Logger log = Logger.getLogger(MeshApp.class);

    private StockTracker stockTracker;
    private ObjectMapper objectMapper;
    private List<String> formatNames;
    private String url;
    private MonitorMetrics monitorMetrics;

    public MeshApp(StockTracker stockTracker, List<String> formatNames, String url) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
        this.objectMapper = new ObjectMapper();
        this.url = url;
        this.monitorMetrics = new MonitorMetrics(WebhookType.MESH, url);
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            monitorMetrics.ban();
            return;
        }

        String responseString = basicHttpResponse.getBody().get();

        try {
            FpProduct fpProduct = objectMapper.readValue(responseString, FpProduct.class);
            monitorMetrics.success();

            List<String> sizes = new ArrayList<>();
            for(String key : fpProduct.getOptions().keySet()){
                FpOption fpOption = fpProduct.getOptions().get(key);
                if(fpOption.getStockStatus().equals("IN STOCK")){
                    sizes.add(fpOption.getSize());
                }
            }

            if(sizes.isEmpty()){
                stockTracker.setOOS(fpProduct.getID());
                return;
            }

            if(stockTracker.notifyForObject(fpProduct.getID(), false)){
                FootpatrolBuilder.buildAttachments(attachmentCreater, url + "/products/rt/" + fpProduct.getID(), fpProduct.getMainImage(), "Mesh", fpProduct.getName(), formatNames);
            }
        } catch (Exception e){
            monitorMetrics.error();
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }

    }
}
