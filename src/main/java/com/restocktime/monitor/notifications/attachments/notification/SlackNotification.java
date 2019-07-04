package com.restocktime.monitor.notifications.attachments.notification;

import com.restocktime.monitor.config.model.notifications.SlackObj;
import com.restocktime.monitor.notifications.model.slack.Attachment;

import java.util.ArrayList;
import java.util.List;

public class SlackNotification {
    private SlackObj slackObj;
    private List<Attachment> attachment;
    private List<String> messages;

    public SlackNotification(SlackObj slackObj, List<Attachment> attachment) {
        this.slackObj = slackObj;
        this.attachment = attachment;
        this.messages = new ArrayList<>();
    }

    public SlackNotification(SlackObj slackObj, List<Attachment> attachment, List<String> messages) {
        this.slackObj = slackObj;
        this.attachment = attachment;
        this.messages = messages;
    }



    public SlackObj getSlackObj() {
        return slackObj;
    }

    public void setSlackObj(SlackObj slackObj) {
        this.slackObj = slackObj;
    }

    public List<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Attachment> attachment) {
        this.attachment = attachment;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
