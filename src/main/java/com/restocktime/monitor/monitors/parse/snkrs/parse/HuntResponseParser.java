package com.restocktime.monitor.monitors.parse.snkrs.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.helper.httprequests.ResponseValidator;
import com.restocktime.monitor.helper.httprequests.model.BasicHttpResponse;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.snkrs.attachment.HuntBuilder;
import com.restocktime.monitor.monitors.parse.snkrs.model.scratch.Images;
import com.restocktime.monitor.monitors.parse.snkrs.model.scratch.Hunt;
import com.restocktime.monitor.monitors.parse.snkrs.model.scratch.Hunts;
import com.restocktime.monitor.monitors.parse.snkrs.model.scratch.stash.StashObj;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.Base64;
import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class HuntResponseParser implements AbstractResponseParser {
    final static Logger logger = Logger.getLogger(HuntResponseParser.class);
    private StockTracker stockTracker;
    private List<String> formatNames;

    public HuntResponseParser(StockTracker stockTracker, List<String> formatNames) {
        this.stockTracker = stockTracker;
        this.formatNames = formatNames;
    }

    public void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) {
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return;
        }

        String responseString = basicHttpResponse.getBody().get();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Hunts hunts = objectMapper.readValue(responseString, Hunts.class);
            logger.info(hunts.getObjects().size());

            for (Hunt s : hunts.getObjects()) {
                if (!stockTracker.notifyForObject(s.getId(), isFirst)) {
                    continue;
                }

                byte[] decoded = Base64.getMimeDecoder().decode(s.getMetadata());
                String output = new String(decoded);
                Images images = null;
                StashObj stashObj = null;
                boolean found = false;



                String type = getHuntType(s.getType());
                if(type.equals("SCRATCHER")) {

                    try {
                        images = objectMapper.readValue(output, Images.class);
                        new URL(images.getTopImage());
                        new URL(images.getBaseImage());
                        found = true;
                    } catch (Exception e) {
                    }
                    if(found) {
                        HuntBuilder.buildAttachments(attachmentCreater, type, true, images.getTopImage(), images.getBaseImage(), "US", formatNames);
                    }
                } else {
                    HuntBuilder.buildAttachments(attachmentCreater, type, false, null, null, "US", formatNames);
                }
                    /*else if(type.equals("STASH")){
                    try {
                        stashObj = objectMapper.readValue(output, StashObj.class);
                        new URL(stashObj.getInviteImageUrl());
                        found = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if(found){
                        List<SlackField> slackFields = new ArrayList<>();
                        List<DiscordField> discordDiscordFields = new ArrayList<>();
                        slackFields.add(new SlackField("Description", stashObj.getInvite()));
                        discordDiscordFields.add(new DiscordField("Description", stashObj.getInvite(), false));
                        for(Stash stash : stashObj.getLocations().getStashes()){
                            slackFields.add(new SlackField("Location", stash.getDisplayText()));
                            discordDiscordFields.add(new DiscordField("Location", stash.getDisplayText(), false));
                        }
                     //   attachmentCreater.addMessages("", "SNKRS STASH: " + stashObj.getTheme(), "Hunt", slackFields, discordDiscordFields, stashObj.getInviteImageUrl());
                    }
                } else {
                  //  attachmentCreater.addMessages("", "NEW SNKRS HUNT LOADED: " + type, "Hunt", null, null);
                }*/

            }
        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);

        }
    }

    private String getHuntType(String huntId){
        if(huntId.equals("26f5ddb3-c597-43da-9680-8dade5af6201")){
            return "SNKRS PASS";
        } else if(huntId.equals("099773ec-45e1-4957-8bc7-b394aad96955")){
            return "SCRATCHER";
        } else if(huntId.equals("caeb1dba-e676-4e45-b581-51668805cb8c")){
            return "SNKRS CAM";
        } else if(huntId.equals("e37fe24a-1ae9-11e7-93ae-92361f002671")){
            return "STASH";
        } else if(huntId.equals("d7b9583d-9098-4e41-a570-3a698cb62f75")){
            return "POINTLESS NONSENSE";
        } else {
            return "N/A";
        }
    }
}
