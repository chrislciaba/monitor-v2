package com.restocktime.monitor.monitors.parse.mesh.parse;

import com.restocktime.monitor.util.httprequests.ResponseValidator;
import com.restocktime.monitor.util.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.util.log.DiscordLog;
import com.restocktime.monitor.util.metrics.MonitorMetrics;
import com.restocktime.monitor.util.stocktracker.StockTracker;

import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.notifications.defaultattachment.DefaultBuilder;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeshFrontEndStockResponseParser implements AbstractResponseParser {
    private static final Logger log = Logger.getLogger(MeshFrontEndStockResponseParser.class);

    private StockTracker stockTracker;
    private String url;
    private String name;
    private final Pattern patternEnglish = Pattern.compile("title=\"Select Size ([^\"]*)\"");
    private final Pattern patternItalian = Pattern.compile("title=\"Seleziona la taglia ([^\"]*)\"");
    private final Pattern patternFrench = Pattern.compile("title=\"Sélectionner une taille ([^\"]*)\"");
    private final Pattern patternSwedish = Pattern.compile("title=\"Välj storlek ([^\"]*)\"");
    private final Pattern patternGermain = Pattern.compile("title=\"Größe wählen ([^\"]*)\"");
    private final Pattern patternDenmark = Pattern.compile("title=\"Vælg Størrelse ([^\"]*)\"");
    private final Pattern patternSpain = Pattern.compile("title=\"Selecciona tu talla ([^\"]*)\"");

    private Pattern pattern;
    private List<String> formatNames;
    private MonitorMetrics monitorMetrics;

    public MeshFrontEndStockResponseParser(StockTracker stockTracker, String url, String name, List<String> formatNames) {
        log.info(url);
        this.stockTracker = stockTracker;
        this.url = url;
        this.name = name;
        this.formatNames = formatNames;
        if (url.contains(".it")) {
            pattern = patternItalian;
        } else if(url.contains(".fr")) {
            pattern = patternFrench;
        } else if(url.contains(".se")) {
            pattern = patternSwedish;
        } else if(url.contains(".de")) {
            pattern = patternGermain;
        } else if(url.contains(".dk")) {
            pattern = patternDenmark;
        } else if(url.contains(".es")) {
            pattern = patternSpain;
        } else {
            pattern = patternEnglish;
        }

       monitorMetrics = new MonitorMetrics(url);
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
           monitorMetrics.error();
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        Matcher sizeMatcher = pattern.matcher(responseString);
        boolean isFirstSize = true;
        String sizes = "";
        while(sizeMatcher.find()){


            String size = sizeMatcher.group(1);
            log.info(size);
            if(!isFirstSize){
                sizes = sizes + ", ";
            } else {
                isFirstSize = false;
            }
            sizes = sizes + size;
        }

        if(sizes.length() > 0){
            monitorMetrics.success();
            if(stockTracker.notifyForObject(url, isFirst)) {
                DefaultBuilder.buildAttachments(attachmentCreater, url, null, "Footpatrol Front End", name, formatNames);
            }
        } else if(sizes.length() == 0){
            monitorMetrics.success();
            stockTracker.setOOS(url);
        }


    }
}
