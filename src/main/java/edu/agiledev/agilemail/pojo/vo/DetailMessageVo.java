package edu.agiledev.agilemail.pojo.vo;

import edu.agiledev.agilemail.pojo.message.AMessage;
import edu.agiledev.agilemail.pojo.message.Attachment;
import edu.agiledev.agilemail.pojo.message.Recipient;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * 邮件详情VO
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/23
 */
@Data
public class DetailMessageVo {

    private Long uid;
    private List<String> from;
    private List<String> replyTo;
    private List<Recipient> recipients;
    private String subject;

    private String datetime;

    private Long size;
    private String content;
    private List<Attachment> attachments;

    private Boolean flagged;
    private Boolean seen;
    private Boolean recent;
    private Boolean deleted;

    private String fromEmailAccount;

    private final static String pattern = "yyyy-MM-dd HH:mm";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

    public static DetailMessageVo from(AMessage m) {
        DetailMessageVo res = new DetailMessageVo();
        res.setUid(m.getUid());
        res.setFrom(m.getFrom());
        res.setSubject(m.getSubject());
        res.setReplyTo(m.getReplyTo());
        res.setRecipients(m.getRecipients());
        res.setSize(m.getSize());
        res.setDatetime(dateFormat.format(Date.from(m.getReceivedDate().toInstant())));
        res.setContent(m.getContent());
        res.setAttachments(m.getAttachments());

        res.setFlagged(m.getFlagged());
        res.setSeen(m.getSeen());
        res.setRecent(m.getRecent());
        res.setDeleted(m.getDeleted());

        return res;
    }

}
