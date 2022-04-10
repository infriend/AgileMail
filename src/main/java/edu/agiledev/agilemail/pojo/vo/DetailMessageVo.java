package edu.agiledev.agilemail.pojo.vo;

import edu.agiledev.agilemail.pojo.message.AMessage;
import edu.agiledev.agilemail.pojo.message.Attachment;
import edu.agiledev.agilemail.pojo.message.Recipient;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Data
public class DetailMessageVo {

    private Long uid;
    private List<String> from;
    private List<String> replyTo;
    private List<Recipient> recipients;
    private String subject;
    private ZonedDateTime receivedDate;
    private Long size;
    private Boolean flagged;
    private Boolean seen;
    private Boolean recent;
    private Boolean deleted;
    private String content;
    private List<Attachment> attachments;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    private String fromEmailAccount;

    public static DetailMessageVo from(AMessage m) {
        DetailMessageVo res = new DetailMessageVo();
        res.setUid(m.getUid());
        res.setFrom(m.getFrom());
        res.setSubject(m.getSubject());
        res.setReplyTo(m.getReplyTo());
        res.setRecipients(m.getRecipients());
        res.setSize(m.getSize());
        res.setDatetime(Date.from(m.getReceivedDate().toInstant()));
        res.setContent(m.getContent());
        res.setAttachments(m.getAttachments());

        res.setFlagged(m.getFlagged());
        res.setSeen(m.getSeen());
        res.setRecent(m.getRecent());
        res.setDeleted(m.getDeleted());

        return res;
    }

}
