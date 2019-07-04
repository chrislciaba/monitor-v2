package com.restocktime.monitor.helper.keywords.notifications;

import com.restocktime.monitor.notifications.model.discord.Embed;
import com.restocktime.monitor.notifications.model.slack.Attachment;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttachmentCreaterTest {
 /*   private AttachmentCreater attachmentCreater;

    @Before
    public void setyp(){
      //  attachmentCreater = new AttachmentCreater();
    }

    @Test
    public void testAttachmentCreatorWithoutImageOrFields(){
        attachmentCreater.addMessages("url", "name", "site", null, null);
        assertEquals(attachmentCreater.getSlackAttachments().size(), 1);
        assertEquals(attachmentCreater.getEmbeds().size(), 1);

        Attachment slackAttachment = attachmentCreater.getSlackAttachments().get(0);
        assertEquals(slackAttachment.getText(), "site");
        assertEquals(slackAttachment.getFallback(), "name (site)");
        assertEquals(slackAttachment.getColor(), "#36a64f");
        assertEquals(slackAttachment.getTitle(), "name");
        assertEquals(slackAttachment.getTitle_link(), "url");
        assertEquals(slackAttachment.getImage_url(), "");
        assertEquals(slackAttachment.getThumb_url(), "");
        assertEquals(slackAttachment.getFooter(), "RestockTime");
        assertEquals(slackAttachment.getFooter_icon(), "");
        assertEquals(slackAttachment.getFields().size(), 0);

        Embed discordEmbed = attachmentCreater.getEmbeds().get(0);
        assertEquals(discordEmbed.getUrl(), "url");
        assertEquals(discordEmbed.getDescription(), "site");
        assertEquals(discordEmbed.getFields(), null);
        assertEquals(discordEmbed.getTitle(), "name");
    }*/

}
