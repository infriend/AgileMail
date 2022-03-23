package edu.agiledev.agilemail.pojo.message;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import edu.agiledev.agilemail.exception.BaseException;
import lombok.Data;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Message模型
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
@Data
public class AMessage implements Serializable {

    private static final long serialVersionUID = -1068972394742882009L;

    private static final String CET_ZONE_ID = "CET";
    public static final String HEADER_IN_REPLY_TO = "In-Reply-To";
    public static final String HEADER_REFERENCES = "References";
    public static final String HEADER_LIST_UNSUBSCRIBE = "List-Unsubscribe";

    private Long uid;
    private String messageId;
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
    private List<String> references;
    private List<String> inReplyTo;
    private List<String> listUnsubscribe;

    /**
     * 将{@link IMAPMessage}映射为{@link Message}.
     * <p>
     * 该方法应该仅仅映射那些使用IMAP fetch命令获得的字段（比如ENVELOP，UID，FLAGS）
     * <p>
     * 其他字段使用其他方法
     *
     * @param folder      message所在的文件夹
     * @param imapMessage message原先形式
     * @return mapped Message with fulfilled envelope fields
     */
    public static <F extends Folder & UIDFolder> AMessage from(
            F folder, IMAPMessage imapMessage) {

        final AMessage res;
        if (imapMessage != null) {
            try {
                res = new AMessage();
                res.setUid(folder.getUID(imapMessage));
                res.setMessageId(imapMessage.getMessageID());
                res.setFrom(processAddress(imapMessage.getFrom()));
                res.setReplyTo(processAddress(imapMessage.getReplyTo()));
                // Process only recipients received in ENVELOPE (don't use getAllRecipients)
                res.setRecipients(Stream.of(
                        processAddress(Message.RecipientType.TO, imapMessage.getRecipients(Message.RecipientType.TO)),
                        processAddress(Message.RecipientType.CC, imapMessage.getRecipients(Message.RecipientType.CC)),
                        processAddress(Message.RecipientType.BCC, imapMessage.getRecipients(Message.RecipientType.BCC))
                ).flatMap(Collection::stream).collect(Collectors.toList()));
                res.setSubject(imapMessage.getSubject());
                res.setReceivedDate(imapMessage.getReceivedDate().toInstant().atZone(ZoneId.of(CET_ZONE_ID)));
                res.setSize(imapMessage.getSizeLong());
                res.setInReplyTo(Arrays.asList(
                        Optional.ofNullable(imapMessage.getHeader(HEADER_IN_REPLY_TO)).orElse(new String[0])));
                res.setReferences(Arrays.asList(
                        Optional.ofNullable(imapMessage.getHeader(HEADER_REFERENCES)).orElse(new String[0])));
                res.setListUnsubscribe(Arrays.asList(
                        Optional.ofNullable(imapMessage.getHeader(HEADER_LIST_UNSUBSCRIBE)).orElse(new String[0])));
                final Flags flags = imapMessage.getFlags();
                res.setFlagged(flags.contains(Flags.Flag.FLAGGED));
                res.setSeen(flags.contains(Flags.Flag.SEEN));
                res.setRecent(flags.contains(Flags.Flag.RECENT));
                res.setDeleted(flags.contains(Flags.Flag.DELETED));
            } catch (MessagingException e) {
                throw new BaseException("解析IMAP Message失败", e);
            }
        } else {
            res = null;
        }
        return res;
    }


    /**
     * 将邮箱地址解析为收件人列表
     */
    private static List<Recipient> processAddress(Message.RecipientType type, Address... addresses) {
        return processAddress(addresses).stream()
                .map(addr -> new Recipient(type.toString(), addr))
                .collect(Collectors.toList());
    }

    /**
     * 将地址解析为字符串，比如person(person@mail.com)
     */
    private static List<String> processAddress(Address... addresses) {
        return Stream.of(Optional.ofNullable(addresses).orElse(new Address[0]))
                .map(address -> {
                    if (address instanceof InternetAddress) {
                        final InternetAddress internetAddress = (InternetAddress) address;
                        return internetAddress.getPersonal() == null ? internetAddress.getAddress() :
                                String.format("\"%s\"<%s>", internetAddress.getPersonal(), internetAddress.getAddress());
                    } else {
                        return address.toString();
                    }
                })
                .collect(Collectors.toList());
    }
}
