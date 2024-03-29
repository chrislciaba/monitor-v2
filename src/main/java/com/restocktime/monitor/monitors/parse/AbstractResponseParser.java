package com.restocktime.monitor.monitors.parse;

import com.restocktime.monitor.util.http.request.model.BasicHttpResponse;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;

public interface AbstractResponseParser {

    void parse(BasicHttpResponse basicHttpResponse, AttachmentCreater attachmentCreater, boolean isFirst) throws Exception;
}
