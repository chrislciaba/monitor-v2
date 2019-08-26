package com.restocktime.monitor.monitors.parse.important.nike.snkrs.model.snkrs.snkrsv2;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {
    private String title;
    private String subtitle;
    private List<Action> actions;
    private String portraitURL;
}
