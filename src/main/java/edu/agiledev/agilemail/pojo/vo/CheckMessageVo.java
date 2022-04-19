package edu.agiledev.agilemail.pojo.vo;

import edu.agiledev.agilemail.pojo.message.AMessage;
import edu.agiledev.agilemail.pojo.message.Recipient;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 邮件预览VO
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/23
 */
@Data
public class CheckMessageVo {
    private Long uid;
    private List<String> from;
    private List<String> replyTo;
    private List<Recipient> recipients;
    private String subject;

    private String datetime;

    private boolean flagged;
    private boolean seen;
    private boolean recent;
    private boolean deleted;

    private String fromEmailAccount;

    private final static String pattern = "yyyy-MM-dd HH:mm";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

    public static CheckMessageVo from(AMessage message) {
        CheckMessageVo res = new CheckMessageVo();

        res.setUid(message.getUid());
        res.setFrom(message.getFrom());
        res.setReplyTo(message.getReplyTo());
        res.setRecipients(message.getRecipients());
        res.setSubject(message.getSubject());

        Date datetime = Date.from(message.getReceivedDate().toInstant());
        res.setDatetime(dateFormat.format(datetime));

        res.setFlagged(message.getFlagged());
        res.setRecent(message.getRecent());
        res.setSeen(message.getSeen());
        res.setDeleted(message.getDeleted());

        return res;
    }

}
