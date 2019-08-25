package com.restocktime.monitor.monitors.ingest.important.mesh;

import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.important.footdistrict.FootDistrict;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.Notifications;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.bot.protection.hawk.Hawk;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.util.*;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;
import static java.lang.System.exit;

public class MeshAppMonitor extends AbstractMonitor {

    final static Logger log = Logger.getLogger(FootDistrict.class);
    private static final String URL_TEMPLATE = "https://prod.jdgroupmesh.cloud/stores/%s/products/%s?channel=android-app&expand=variations,informationBlocks,customisations";
    private String url;
    private int delay;
    private String store;

    Map<String, String> cookieList;
    private AttachmentCreater attachmentCreater;
    private HttpRequestHelper httpRequestHelper;
    private Hawk hawk;
    private AbstractResponseParser abstractResponseParser;

    public MeshAppMonitor(
            String sku,
            String store,
            int delay,
            AttachmentCreater attachmentCreater,
            HttpRequestHelper httpRequestHelper,
            Hawk hawk, AbstractResponseParser abstractResponseParser){
        this.url = String.format(URL_TEMPLATE, store, sku);
        this.store = store;
        this.delay = delay;
        this.cookieList = new HashMap<>();
        this.attachmentCreater = attachmentCreater;
        this.httpRequestHelper = httpRequestHelper;
        this.hawk = hawk;
        this.abstractResponseParser = abstractResponseParser;
    }


    public void run(BasicRequestClient basicRequestClient, boolean isFirst){
        attachmentCreater.clearAll();
        //Timeout.timeout(delay);

        try{
            Optional<Integer> existingIdx = Optional.empty();
            for(int i = 0; i < basicRequestClient.getHeaderList().size(); i++){
                Header h = basicRequestClient.getHeaderList().get(i);
                if(h.getName().equals("X-Request-Auth")){
                    existingIdx = Optional.of(i);
                    break;
                }
            }

            List<Header> newList = new ArrayList<>(basicRequestClient.getHeaderList());
            if (existingIdx.isPresent()) {
                newList.remove(existingIdx.get());

            }



            newList.add(new BasicHeader("X-Request-Auth", hawk.createHawkHeader(store, url)));

            basicRequestClient.setHeaderList(newList);
            BasicHttpResponse basicHttpResponse = httpRequestHelper.performGet(basicRequestClient,url);
            log.info(basicHttpResponse.getBody().get());
            exit(1);

            //abstractResponseParser.parse(basicHttpResponse, attachmentCreater, isFirst);
            Notifications.send(attachmentCreater);
        } catch(Exception e){
            log.error(EXCEPTION_LOG_MESSAGE, e);
        }
    }

    public String getUrl(){
        return url;
    }
}
