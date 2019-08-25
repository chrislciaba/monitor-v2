package com.restocktime.monitor.monitors.parse.important.offwhite.helper;

import com.restocktime.monitor.util.http.request.ResponseValidator;
import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.monitors.parse.important.offwhite.model.offwhite.OffWhiteThreadObj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetLinksFromPage {

    private String name;

    private final String patternStr = "<a itemProp=\"url\" href=\"([^\"]*)\"><span content='([^\']*)' itemProp='name' style='display:none'></span>";
    /*\\s+"+
            "<span content='[^']*'' itemProp='brand' style='display:none'></span>\\s+"+
            "<span content='[^']*' itemProp='model' style='display:none'></span>\\s+"+
            "<figure>\\s+"+
            "<img itemProp=\"image\" alt=\"[^\"]*\" class=\"top\" src=\"([^\"]*)\" />\\s+"+
            "<figcaption>\\s+"+
            "<div class='brand-name'>\\s+"+
            "([^>]*)\\s+"+
            "</div>";*/

    private Pattern pattern = Pattern.compile(patternStr);

    public GetLinksFromPage(String name) {
        this.name = name;
    }

    public OffWhiteThreadObj getLinks(BasicHttpResponse basicHttpResponse, boolean isFirst){
        if (ResponseValidator.isInvalid(basicHttpResponse)) {
            return null;
        }

        String responseString = basicHttpResponse.getBody().get();

        Matcher m = pattern.matcher(responseString);

        while(m.find()){
            String href = m.group(1);
            String tag = m.group(2); //name
            if(tag.equals(name)){
                return new OffWhiteThreadObj(tag, "https://www.off---white.com" + href);
            }
        }


        return null;
    }
}