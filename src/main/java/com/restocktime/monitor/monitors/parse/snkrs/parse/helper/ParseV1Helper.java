package com.restocktime.monitor.monitors.parse.snkrs.parse.helper;

import com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv1.Content;
import com.restocktime.monitor.monitors.parse.snkrs.model.snkrs.snkrsv1.Thread;
import com.restocktime.monitor.monitors.parse.snkrs.model.ParsedResponse;

import java.util.ArrayList;
import java.util.List;

public class ParseV1Helper {

    private Content content;
    private final String SKU_TEMPLATE = "%s-%s";

    public ParseV1Helper(Content content) {
        this.content = content;
    }

    private String getId(Thread thread){
        return thread.getId();
    }

    private String getName(Thread thread){
        return thread.getSeoTitle();
    }

    private String getLink(Thread thread){
        return thread.getSeoSlug();
    }

    private String getType(Thread thread){
        for(String tag : thread.getTags()){
            if(tag.equals("SNKRS PASS")){
                return "SNKRS PASS";
            } else if(tag.equals("BEHIND THE DESIGN")){
                return "BEHIND THE DESIGN";
            }
        }

        return null;
    }

    private String getImage(Thread thread){
        return thread.getImageUrl();
    }

    private String getSelectionEngine(Thread thread){
        if(thread.getProduct() != null && thread.getProduct().getSelectionEngine() != null){
            return thread.getProduct().getSelectionEngine();
        }

        return null;
    }

    private String getSku(Thread thread){
        if(thread.getProduct() != null && thread.getProduct().getStyle() != null && thread.getProduct().getColorCode() != null){
            return String.format(SKU_TEMPLATE, thread.getProduct().getStyle(), thread.getProduct().getColorCode());
        }

        return null;
    }

    private String getStartDate(Thread thread){
        if(thread.getProduct() != null && thread.getProduct().getStartSellDate() != null){
            return thread.getProduct().getStartSellDate();
        }

        return null;
    }

    public ParsedResponse getParsedResponse(Thread thread){
        String type = getType(thread);
        String getMethod = getSelectionEngine(thread);

        return new ParsedResponse(getId(thread), getName(thread), getLink(thread), getType(thread), getImage(thread), getSelectionEngine(thread), DateHelper.formatDate(getStartDate(thread)), DateHelper.getEpochTimeSecs(getStartDate(thread)), getSku(thread), true);
    }

    public List<ParsedResponse> getParsedResponseList(){
        List<ParsedResponse> parsedResponses = new ArrayList<>();
        for(Thread thread : content.getThreads()){
            parsedResponses.add(getParsedResponse(thread));
        }

        return parsedResponses;
    }
}
