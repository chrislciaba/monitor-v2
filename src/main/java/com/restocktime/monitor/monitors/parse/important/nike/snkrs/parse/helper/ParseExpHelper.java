package com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.helper;



import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.ParsedResponse;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.exp.ExpSnkrs;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.exp.Thread;

import java.util.ArrayList;
import java.util.List;

public class ParseExpHelper {
    private ExpSnkrs expSnkrs;
    private final String SKU_TEMPLATE = "%s-%s";

    public ParseExpHelper(ExpSnkrs expSnkrs) {
        this.expSnkrs = expSnkrs;
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
        if(thread.getRestricted())
            return "EXCLUSIVE ACCESS";

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
        return ParsedResponse.builder()
                .id(getId(thread))
                .name(getName(thread))
                .slug(getLink(thread))
                .type(getType(thread))
                .image(getImage(thread))
                .selectionEngine(getSelectionEngine(thread))
                .launchDate(DateHelper.formatDate(getStartDate(thread)))
                .launchDateEpoch(DateHelper.getEpochTimeSecs(getStartDate(thread)))
                .sku(getSku(thread))
                .available(true)
                .build();
    }

    public List<ParsedResponse> getParsedResponseList(){
        List<ParsedResponse> parsedResponses = new ArrayList<>();
        for(Thread thread : expSnkrs.getThreads()){
            parsedResponses.add(getParsedResponse(thread));
        }

        return parsedResponses;
    }
}
